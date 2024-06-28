package it.polimi.ingsw.Server.Model.Lobby;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Exceptions.Server.LobbyExceptions.*;
import it.polimi.ingsw.Exceptions.Server.PermissionExceptions.AdminRoleRequiredException;
import it.polimi.ingsw.Server.Model.Game.Logic.Game;
import it.polimi.ingsw.Server.Controller.InputHandler.GameControllerObserver;
import it.polimi.ingsw.Server.Model.Game.GameLoader;
import it.polimi.ingsw.Server.Controller.GameController;
import it.polimi.ingsw.Server.Interfaces.ServerModelLayer;
import it.polimi.ingsw.Server.Model.Server.LobbyPreviewObserverRelay;
import it.polimi.ingsw.Server.Model.Server.ServerModel;
import it.polimi.ingsw.Server.Model.Server.ServerUser;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.*;

import java.util.*;
import java.util.logging.Logger;

/**
 * This class represents a lobby in the server, managing user connections, game control, and messaging within the lobby.
 */
public class Lobby implements ServerModelLayer {
    //ATTRIBUTES
    private final String lobbyName;
    private final int lobbySize;
    private final ServerModel server;


    private final List<LobbyUserColors> availableUserColors;
    private final Object colorsLock = new Object();



    //Attributes that should always be synchronized to usersLock
    private final Map<String, LobbyUser> lobbyUsers;
    //the lobby is set to closed when the game starts or when the max player amount is reached.
    private boolean lobbyClosed;
    //Lock for this group of Attributes
    private final Object usersLock = new Object();

    private GameController gameController;
    private Game game;
    private boolean gameStartable;
    private boolean gameStarted;


    //LISTENERS
    private final Map<String, GameControllerObserver> gameControllerObservers;
    private final LobbyPreviewObserverRelay lobbyPreviewObserverRelay;
    private final ObserverRelay lobbyObserver;

    private final Logger logger;

    //DISCONNECTION TIMER
    /**
     * Map that stores for each disconnected player their thread containing the count-down for their removal from the lobby
     */
    private final Map<String, Timer> reconnectionTimers;
    private Timer victoryByDefaultTimer;
    private Timer startGameTimer;





    //CONSTRUCTOR

    /**
     * Default constructor, created the lobby and adds the admin user.
     * @param lobbyName         Name of the lobby
     * @param lobbySize Number of players the admin wants to start a game with.
     * @param admin        Admin/creator of the lobby
     * @param ch                Connection associated with the user
     */
    public Lobby(String lobbyName, int lobbySize, ServerUser admin, ClientHandler ch, ServerModel server, LobbyPreviewObserverRelay lobbyPreviewObserverRelay) throws InvalidLobbySettingsException {
        this.server = server;
        logger = Logger.getLogger(Lobby.class.getName());
        logger.info("Initializing lobby "+lobbyName+" with admin: "+admin);


        if(lobbySize > 4 || lobbySize < 2)
            throw new InvalidLobbySettingsException("The value provided for max lobby users is not a valid number.\n" +
                    "Please provide a value between 2 and 4");

        this.lobbySize = lobbySize;
        this.lobbyName = lobbyName;
        this.lobbyUsers = new HashMap<>();
        this.lobbyPreviewObserverRelay = lobbyPreviewObserverRelay;
        lobbyObserver = new ObserverRelay();

        availableUserColors = new ArrayList<>();
        availableUserColors.add(LobbyUserColors.RED);
        availableUserColors.add(LobbyUserColors.BLUE);
        availableUserColors.add(LobbyUserColors.GREEN);
        availableUserColors.add(LobbyUserColors.YELLOW);

        gameControllerObservers = new HashMap<>();
        gameStarted = false;
        lobbyClosed = false;


        reconnectionTimers = new HashMap<>();
        victoryByDefaultTimer = null;
        startGameTimer = null;

        LobbyUser lobbyUser = new LobbyUser(admin, LobbyRoles.ADMIN);
        addLobbyUserToLobby(ch, lobbyUser);

        logger.fine("Sending success lobby created to user");
        lobbyObserver.update(lobbyUser.getUsername(), new SCPStartLobbySuccess(toLobbyRecord(), toLobbyUserRecord()));

        logger.fine("Lobby initialized");
    }

    /**
     * Adds a new lobby user to the lobby with the provided client handler and assigns them a random color.
     * Updates lobby previews and notifies observers about the changes.
     *
     * @param ch         The client handler associated with the lobby user.
     * @param lobbyUser  The lobby user to add to the lobby.
     */
    private void addLobbyUserToLobby(ClientHandler ch, LobbyUser lobbyUser){
        logger.info("Adding user "+lobbyUser.getUsername()+" to lobby");

        synchronized (usersLock) {
            logger.fine("Adding lobby user "+lobbyUser.getUsername()+" to users map and to observer");
            //Links the connection to the lobby user. The link will be used during the re-connection phase.
            String username = lobbyUser.getUsername();
            lobbyUsers.put(username, lobbyUser);

            if(lobbyUsers.size() > 1)
                gameStartable = true;

            if (lobbyUsers.size() == lobbySize)
                lobbyClosed = true;
            logger.fine("lobby user "+lobbyUser.getUsername()+" added");
        }

        //Pick random color for the user
        synchronized (colorsLock) {
            logger.fine("Assigning lobby user "+lobbyUser.getUsername()+" a color");
            Random rand = new Random();
            int random = rand.nextInt(0, availableUserColors.size());
            lobbyUser.setColor(availableUserColors.remove(random));
            logger.fine("Lobby user "+lobbyUser.getUsername()+" is: "+lobbyUser.getColor());
        }

        logger.fine("Updating lobby preview");
        lobbyPreviewObserverRelay.updateLobbyPreview(toLobbyPreview());
        lobbyObserver.update(new SCPUpdateLobby(toLobbyRecord()));
        lobbyObserver.update(new SCPUpdateLobbyUsers(toLobbyUserRecord()));


        String username = lobbyUser.getUsername();
        lobbyObserver.subscribe(username, ch);
        lobbyObserver.update(username, new SCPJoinLobbySuccessful(toLobbyRecord(), toLobbyUserRecord()));

        if(lobbyUsers.size() == lobbySize){
            startGameTimer = new Timer("startGameTimer");
            startGameTimer.schedule(getStartGameTask(), LobbyConstants.gameStartTimerLength);
            System.out.println("Game will start in "+(LobbyConstants.gameStartTimerLength/1000)+" seconds in lobby: "+lobbyName);
            logger.info("Game start timer started in lobby: "+lobbyName);
        }
    }





    //JOINING METHODS
    /**
     * Allows a user to join the lobby.
     *
     * @param serverUser                            The user trying to join.
     * @param ch                                    The client handler associated with the user.
     * @throws LobbyClosedException                 If the lobby is closed.
     * @throws LobbyUserAlreadyConnectedException   If the user is already connected to the lobby.
     */
    public void joinLobby(ServerUser serverUser, ClientHandler ch) throws LobbyClosedException, LobbyUserAlreadyConnectedException {

        //If the user was already in the lobby then call reconnection procedure
        synchronized (usersLock) {
            if (lobbyUsers.containsKey(serverUser.getUsername())){
                LobbyUser lobbyUser = lobbyUsers.get(serverUser.getUsername());
                reconnect(lobbyUser, ch);
                //todo
                return;
            }
        }

        //If the game had already started then throw exception
        if(lobbyClosed){
            throw new LobbyClosedException("Can't join lobby, the lobby is closed.");
        }

        logger.info("User "+serverUser.getUsername()+" is joining Lobby "+lobbyName);

        //If none of the above scenarios were true then proceed to add new user to lobby
        LobbyUser lobbyUser = new LobbyUser(serverUser, LobbyRoles.GUEST);

        addLobbyUserToLobby(ch, lobbyUser);
    }

    /**
     * Handles the reconnection procedure for a lobby user who was previously disconnected.
     * If the user is not disconnected, throws a {@link LobbyUserAlreadyConnectedException}.
     * Cancels any active reconnection timers and resets the victory by default timer if present.
     * Updates the lobby observer with the new connection source and notifies about the user's status change.
     * Initiates the user reconnection procedure in the associated game.
     *
     * @param lobbyUser  The lobby user attempting to reconnect.
     * @param ch         The client handler associated with the reconnection.
     * @throws LobbyUserAlreadyConnectedException If the user is already connected to the lobby.
     */
    private void reconnect(LobbyUser lobbyUser, ClientHandler ch) throws LobbyUserAlreadyConnectedException {

        //If reconnecting to user that is not actually DISCONNECTED throw exception
        if(!lobbyUser.getConnectionStatus().equals(LobbyUserConnectionStates.OFFLINE)){
            throw new LobbyUserAlreadyConnectedException("user "+lobbyUser.getUsername()+" is already connected to "+lobbyName);
        }

        logger.info("User "+lobbyUser.getUsername()+" is reconnecting to Lobby "+lobbyName);

        //Interrupt the disconnection timer
        if(reconnectionTimers.containsKey(lobbyUser.getUsername())) {
            Timer reconnectionTimer = reconnectionTimers.remove(lobbyUser.getUsername());
            reconnectionTimer.cancel();
            reconnectionTimer.purge();
        }

        if(victoryByDefaultTimer!=null) {
            victoryByDefaultTimer.cancel();
            victoryByDefaultTimer.purge();
            victoryByDefaultTimer = null;
        }

        //Update with new connection source and set them back online
        lobbyObserver.subscribe(lobbyUser.getUsername(), ch);
        lobbyUser.setOnline();

        //notify of user status change
        lobbyObserver.update(lobbyUser.getUsername(), new SCPJoinLobbySuccessful(toLobbyRecord(), toLobbyUserRecord()));
        lobbyObserver.update(new SCPUpdateLobbyUsers(toLobbyUserRecord()));

        game.userReconnectionProcedure(lobbyUser.getUsername());
    }





    //QUITTING METHODS
    /**
     * Handles the abrupt disconnection procedure for a user.
     *
     * @param username The user disconnecting.
     */
    @Override
    public void userDisconnectionProcedure(String username) {
        logger.info("Starting disconnection procedure for user "+username+" in lobby "+lobbyName);
        System.out.println("Starting disconnection procedure for user "+username+" in lobby "+lobbyName);

        LobbyUser lobbyUser = lobbyUsers.get(username);

        lobbyUser.setOffline();
        synchronized (usersLock) {
            logger.fine("removing lobby user "+username+" from observers");
            lobbyObserver.unsubscribe(username);
            removeGameControllerObserver(username);
        }

        lobbyObserver.update(new SCPUpdateLobbyUsers(toLobbyUserRecord()));

        logger.info("Deciding which removal procedure to apply to "+username);

        //If the game has not started, or the game has started but allows for direct player removal
        // then the disconnection needs to only be handled on the lobby level with the timer
        if(!gameStarted || game.shouldRemovePlayerOnDisconnect()) {

            if(gameStarted){
                logger.info("Disconnection method for "+username+": Standard Game + Lobby disconnection");
                game.userDisconnectionProcedure(username);

                logger.fine("Setting up lobby disconnection timer");
                Timer lobbyDisconnectionTimer = new Timer("lobbyDisconnectionTimer");
                reconnectionTimers.put(lobbyUser.getUsername(), lobbyDisconnectionTimer);
                lobbyDisconnectionTimer.schedule(getDisconnectUserTimerTask(lobbyUser), LobbyConstants.disconnectionTimerLength);
                logger.info("Disconnection timer started in lobby "+lobbyName+" for user: "+username);
            }
            else {
                logger.info("Disconnection method for " + username + ": Standard Lobby disconnection.\nProceeding to remove from lobby.");
                removeUser(lobbyUser);
            }

        }

        //If the game has started and it is in a phase that does not allow for player removal and there are more than one
        //players left online then you DON'T remove the player from the lobby and only start the user disconnection procedure in the game.
        else if(!game.shouldRemovePlayerOnDisconnect()){
            logger.info("Disconnection method for "+username+": Disconnection without removal");
            game.userDisconnectionProcedure(username);
            logger.fine("Finished game disconnection procedure, continuing in lobby");

            logger.info("Entering tryForVictoryByDefault() method");
            tryForVictoryByDefault();
        }
    }

    /**
     * Allows a user to quit the lobby.
     *
     * @param username The user quitting the lobby.
     */
    @Override
    public void quit(String username){
        LobbyUser lobbyUser = lobbyUsers.get(username);
        removeUser(lobbyUser);

        System.out.println("User "+username+" has quit from the lobby");
        if(gameStarted) {
            if(!game.shouldRemovePlayerOnDisconnect())
                tryForVictoryByDefault();
            game.quit(username);
        }
    }

    /**
     * Checks if victory by default conditions are met when a user disconnects during the game.
     * If only one user remains online in the lobby, starts a victory by default timer.
     * This method is triggered during user disconnection events to potentially end the game.
     */
    private void tryForVictoryByDefault(){
        if(!gameStarted)
            return;

        int onlineCounter = 0;
        for(LobbyUser user : lobbyUsers.values())
            if(user.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE))
                onlineCounter++;
        logger.fine(onlineCounter+" left online");

        if(onlineCounter == 1){
            logger.info("1 user left online detected, starting victory by default timer in lobby: "+lobbyName);
            victoryByDefaultTimer = new Timer("victoryByDefaultTimer");
            victoryByDefaultTimer.schedule(getVictoryByDefaultTimerTask(), LobbyConstants.victoryByDefaultTimerLength);
            logger.info("Victory by default timer started in lobby "+lobbyName);
        }
    }

    /**
     * Removes a user from the lobby and performs necessary updates.
     * If the removed user is the lobby's admin, a new admin is randomly assigned if there are other users present.
     * Stops the game start timer if it was running.
     * If the lobby becomes empty after removing the user, it is removed from the server model.
     *
     * @param lobbyUser The LobbyUser to be removed from the lobby.
     */
    private void removeUser(LobbyUser lobbyUser){
        logger.info("Removing user "+lobbyUser.getUsername()+" from lobby "+lobbyName);
        synchronized (usersLock) {
            // Cancel the game start timer if it's running
            if(startGameTimer != null){
                startGameTimer.cancel();
                startGameTimer.purge();
                startGameTimer = null;
                logger.fine("Timer for game started was running and has now been stopped in lobby: "+lobbyName);
                System.out.println("Start game timer has been stopped in lobby: "+lobbyName);
            }

            // Remove the user from lobby data structures
            lobbyUsers.remove(lobbyUser.getUsername());
            availableUserColors.add(lobbyUser.getColor());
            lobbyObserver.unsubscribe(lobbyUser.getUsername());

            // If the removed user was an admin, assign admin role to another user
            if(lobbyUser.getLobbyRole().equals(LobbyRoles.ADMIN)) {
                logger.fine("Removing old admin, generating new admin");
                List<LobbyUser> lobbyUsersList = lobbyUsers.values().stream().toList();
                if(lobbyUsersList.size() > 1) {
                    logger.fine("More than one user detected in lobby, generating new admin randomly");
                    // Choose a new admin randomly from remaining users
                    Random random = new Random();
                    int nextAdminIndex = random.nextInt(lobbyUsersList.size() - 1);
                    lobbyUsersList.get(nextAdminIndex).setAdmin();
                    logger.fine("New admin is user number "+nextAdminIndex+": "+lobbyUsersList.get(nextAdminIndex).getUsername());
                }
                else if(lobbyUsers.size() == 1){
                    logger.fine("Only one user left in the lobby, setting them as admin");
                    lobbyUsersList.getFirst().setAdmin();
                    logger.fine("New admin is user: "+lobbyUsersList.getFirst().getUsername());
                }
            }

            if(lobbyUsers.size() < 2)
                gameStartable = false;
        }
        logger.fine("Lobby user "+lobbyUser.getUsername()+" removed from lobby "+lobbyName);

        //If a user leaves the lobby while the game has not yet started then the lobby becomes join-able again
        //to meet the required max players
        if(!gameStarted)
            lobbyClosed = false;


        if(lobbyUsers.isEmpty()){
            logger.info("No players left in lobby, proceeding to remove lobby "+lobbyName+" from Server model");
            lobbyPreviewObserverRelay.removeLobbyPreview(toLobbyPreview());
            server.removeLobby(lobbyName);
            return;
        }

        //Updating observers
        lobbyPreviewObserverRelay.updateLobbyPreview(toLobbyPreview());
        lobbyObserver.update(new SCPUpdateLobbyUsers(toLobbyUserRecord()));
        lobbyObserver.update(new SCPUpdateLobby(toLobbyRecord()));
    }

    //TIMER TASKS
    /**
     * Creates a TimerTask to handle disconnection timeout for the specified lobby user.
     * The task removes the user from the lobby, removes them from the game if it has started,
     * and cancels any reconnection timer associated with the user.
     *
     * @param lobbyUser The lobby user for whom the disconnection timer task is created.
     * @return TimerTask that handles disconnection timeout for the user.
     */
    private TimerTask getDisconnectUserTimerTask(LobbyUser lobbyUser){

        String username = lobbyUser.getUsername();

        return new TimerTask() {
            @Override
            public void run() {
                logger.info("Disconnection timer is over for "+username);
                removeUser(lobbyUser);

                if(gameStarted)
                    game.removePlayer(username);

                reconnectionTimers.remove(username);
            }
        };
    }

    /**
     * Creates a TimerTask to handle victory by default timeout in the lobby.
     * The task removes offline users from the lobby and the game, and triggers
     * the game over procedure once all offline users are removed.
     *
     * @return TimerTask that handles victory by default timeout in the lobby.
     */
    private TimerTask getVictoryByDefaultTimerTask(){
        return new TimerTask() {
            @Override
            public void run() {
                logger.info("Victory by default timer is over in lobby "+lobbyName);

                List<LobbyUser> lobbyUserList = lobbyUsers.values().stream().toList();

                logger.fine("Removing each offline user from lobby and game");
                for(int i = 0; i < lobbyUserList.size(); i++) {
                    LobbyUser lobbyUser = lobbyUserList.get(i);
                    if (!lobbyUser.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)) {
                        logger.info("Removing user " + i + ": " + lobbyUser.getUsername());
                        removeUser(lobbyUser);
                        game.removePlayer(lobbyUser.getUsername());
                        //No need to decrement counter i after removal as the removal is on the map, the list
                        //over which we iterate does not change after remove, only the map.
                    }
                }
                logger.fine("Starting game over procedure");
                game.gameOver();
            }
        };
    }

    /**
     * Creates a TimerTask to handle starting the game when the timer expires.
     * The task checks if the game has not already started, and if not, starts the game
     * and logs the event.
     *
     * @return TimerTask that handles starting the game when the timer expires.
     */
    private TimerTask getStartGameTask(){
        return new TimerTask() {
            @Override
            public void run() {
                if(!gameStarted) {
                    startGame();
                    logger.info("Game started by timer in Lobby: "+lobbyName);
                }
            }
        };
    }




//todo fix bug not updating game started in previews
    //LOBBY METHODS
    /**
     * Starts the game in the lobby.
     */
    public void startGameManually(String username) throws AdminRoleRequiredException, InvalidLobbySizeToStartGameException, GameAlreadyStartedException {

        if(!lobbyUsers.get(username).getLobbyRole().equals(LobbyRoles.ADMIN))
            throw new AdminRoleRequiredException("You need to be an admin to start the game in this lobby");

        if(lobbyUsers.size() < 2)
            throw new InvalidLobbySizeToStartGameException("You need at least two players in your lobby to start a game.");

        if(game != null)
            throw new GameAlreadyStartedException("The game has already started in this lobby");

        logger.info("Game started manually in lobby: "+lobbyName);
        if(startGameTimer != null) {
            startGameTimer.cancel();
            startGameTimer.purge();
            logger.fine("Starting timer was already running so it has been now stopped in lobby: "+lobbyName);
        }

        startGame();
    }

    /**
     * Starts the game within the lobby. Sets the gameStarted flag to true,
     * closes the lobby, initializes the game with the lobby users, updates
     * the game controller, and updates the lobby preview.
     */
    private void startGame(){
        gameStarted = true;
        lobbyClosed = true;

        game = GameLoader.startNewGame(lobbyUsers.values().stream().toList(), this, lobbyObserver);
        updateGameController(new GameController(game));

        startGameTimer = null;

        lobbyPreviewObserverRelay.updateLobbyPreview(toLobbyPreview());
    }

    /**
     * Handles the game over event in the lobby. Resets game-related variables,
     * cancels victory by default timer if active, removes offline users from the lobby,
     * updates lobby status, and notifies lobby preview observers.
     */
    public void gameOver(){
        updateGameController(null);
        if(victoryByDefaultTimer != null) {
            victoryByDefaultTimer.cancel();
            victoryByDefaultTimer.purge();
        }
        game = null;
        gameStarted = false;

        for(LobbyUser user: new ArrayList<>(lobbyUsers.values())){
            if(user.getConnectionStatus().equals(LobbyUserConnectionStates.OFFLINE))
                removeUser(user);
        }

        System.out.println("Game finished in lobby: "+lobbyName);

        if(!lobbyUsers.isEmpty()) {
            if (lobbyUsers.size() < lobbySize)
                lobbyClosed = false;

            lobbyPreviewObserverRelay.updateLobbyPreview(toLobbyPreview());
        }
    }

    /**
     * Allows the user to change their color to any of the other available colors in the Lobby.
     *
     * @param username      User that wants to change their color.
     * @param newColor  Color which the user wants to change to.
     * @throws UnavailableLobbyUserColorException   If a user selects a color that is not available.
     */
    public void changeColor(String username, LobbyUserColors newColor) throws UnavailableLobbyUserColorException {
        synchronized (colorsLock){
            LobbyUser user = lobbyUsers.get(username);

            LobbyUserColors oldColor = user.getColor();

            if(oldColor.equals(newColor))
                return;

            if(availableUserColors.contains(newColor)){
                availableUserColors.remove(newColor);
                user.setColor(newColor);
                availableUserColors.add(oldColor);
            }
            else
                throw new UnavailableLobbyUserColorException("Color is not available");
        }
        lobbyObserver.update(new SCPUpdateLobbyUsers(toLobbyUserRecord()));
        lobbyObserver.update(new SCPUpdateLobby(toLobbyRecord()));
    }

    /**
     * Sends a chat message in the lobby, either broadcasting it to all lobby users or sending it privately
     * to a specific recipient and the sender.
     *
     * @param chatMessage The chat message record containing sender, recipient, and message details.
     * @throws NoSuchRecipientException   If the recipient specified in the message is not found in the lobby.
     * @throws InvalidRecipientException  If the sender attempts to send a private message to themselves.
     */
    public void sendChatMessage(ChatMessageRecord chatMessage) throws NoSuchRecipientException, InvalidRecipientException {
        logger.info("Sending message in Lobby "+lobbyName+" from user "+chatMessage.getSender());
        if(!chatMessage.isMessagePrivate()) {
            logger.fine("The message is not private, proceeding to broadcast the message to all lobby users");
            lobbyObserver.update(new SCPReceiveMessage(chatMessage));
        }
        else {
            String recipient = chatMessage.getRecipient();
            logger.fine("The message is private, checking if the recipient is in this lobby");

            if(recipient.equals(chatMessage.getSender()))
                throw new InvalidRecipientException("User "+chatMessage.getSender()+" attempted to send a message to themself");

            if(!lobbyUsers.containsKey(recipient))
                throw new NoSuchRecipientException("No recipient with the name of "+recipient+" was found in lobby: "+lobbyName);

            logger.fine("Sending the message to "+recipient);

            lobbyObserver.update(chatMessage.getRecipient(), new SCPReceiveMessage(chatMessage));
            lobbyObserver.update(chatMessage.getSender(), new SCPReceiveMessage(chatMessage));
        }
    }





    //GETTERS
    public String getLobbyName(){
        return lobbyName;
    }
    public GameController getGameController(){
        return gameController;
    }





    //OBSERVER METHODS
    /**
     * Updates the game controller with a new instance and sends the new instance to all subscribed observers.
     *
     * @param newGameController The new game controller instance.
     */
    private void updateGameController(GameController newGameController){
        this.gameController = newGameController;
        for(GameControllerObserver observer : gameControllerObservers.values()){
            observer.updateGameController(newGameController);
        }
    }

    /**
     * Generates a summary record of the lobby's current state for preview purposes.
     *
     * @return A {@link LobbyPreviewRecord} summarizing the lobby's name, current user count, maximum capacity, and game start status.
     */
    private LobbyPreviewRecord toLobbyPreview(){
        return new LobbyPreviewRecord(lobbyName, lobbyUsers.size(), lobbySize, gameStarted);
    }

    /**
     * Adds a game controller observer for a specific user in the lobby.
     *
     * @param username The username of the user to add the observer for.
     * @param observer The {@link GameControllerObserver} instance to observe the game controller.
     */
    public void addGameControllerObserver(String username, GameControllerObserver observer){
        gameControllerObservers.put(username, observer);
    }

    /**
     * Removes the game controller observer associated with a specific user from the lobby.
     *
     * @param username The username of the user whose game controller observer needs to be removed.
     */
    private void removeGameControllerObserver(String username){
        gameControllerObservers.remove(username);
    }





    //TO RECORD METHODS
    /**
     * Converts the current lobby's state into a detailed record format.
     *
     * @return A {@link LobbyRecord} representing the lobby's name, size, available user colors, and game start readiness.
     */
    private LobbyRecord toLobbyRecord(){
        return new LobbyRecord(lobbyName, lobbySize, availableUserColors, gameStartable);
    }

    /**
     * Converts all lobby users into their record representations.
     *
     * @return A list of {@link LobbyUserRecord} objects, each representing a user's details in the lobby.
     */
    private List<LobbyUserRecord> toLobbyUserRecord(){
        List<LobbyUserRecord> lobbyUserRecords = new ArrayList<>();
        for(LobbyUser lobbyUser : lobbyUsers.values())
            lobbyUserRecords.add(lobbyUser.toRecord());

        return lobbyUserRecords;
    }
}
