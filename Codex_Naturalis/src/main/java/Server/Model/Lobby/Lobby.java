package Server.Model.Lobby;

import Client.Model.Records.LobbyPreviewRecord;
import Client.Model.Records.LobbyRecord;
import Client.Model.Records.LobbyUserRecord;
import Exceptions.Server.LobbyExceptions.*;
import Exceptions.Server.PermissionExceptions.AdminRoleRequiredException;
import Model.Game.Game;
import Network.ServerClient.Packets.SCPUpdateLobby;
import Network.ServerClient.Packets.SCPUpdateLobbyUsers;
import Network.ServerClient.Packets.SCPJoinLobbySuccessful;
import Network.ServerClient.Packets.SCPStartLobbySuccess;
import Server.Controller.InputHandler.GameControllerObserver;
import Server.Model.Lobby.Game.GameLoader;
import Server.Controller.GameController;
import Server.Interfaces.ServerModelLayer;
import Server.Model.LobbyPreviewObserverRelay;
import Server.Model.ServerModel;
import Server.Model.ServerUser;
import Server.Network.ClientHandler.ClientHandler;

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
    private final Map<LobbyUser, Thread> reconnectionTimers;
    private Thread victoryByDefaultTimer;





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

        LobbyUser lobbyUser = new LobbyUser(admin, LobbyRoles.ADMIN);
        addLobbyUserToLobby(ch, lobbyUser);

        logger.fine("Sending success lobby created to user");
        lobbyObserver.update(lobbyUser.getUsername(), new SCPStartLobbySuccess(toLobbyRecord(), toLobbyUserRecord()));

        logger.fine("Lobby initialized");
    }

    private void addLobbyUserToLobby(ClientHandler ch, LobbyUser lobbyUser){
        logger.info("Adding user "+lobbyUser.getUsername()+" to lobby");

        synchronized (usersLock) {
            logger.fine("Adding lobby user "+lobbyUser.getUsername()+" to users map and to observer");
            //Links the connection to the lobby user. The link will be used during the re-connection phase.
            String username = lobbyUser.getUsername();
            lobbyUsers.put(username, lobbyUser);

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

    private void reconnect(LobbyUser lobbyUser, ClientHandler ch) throws LobbyUserAlreadyConnectedException {

        //If reconnecting to user that is not actually DISCONNECTED throw exception
        if(!lobbyUser.getConnectionStatus().equals(LobbyUserConnectionStates.OFFLINE)){
            throw new LobbyUserAlreadyConnectedException("user "+lobbyUser.getUsername()+" is already connected to "+lobbyName);
        }

        logger.info("User "+lobbyUser.getUsername()+" is reconnecting to Lobby "+lobbyName);

        //Interrupt the disconnection timer
        if(reconnectionTimers.containsKey(lobbyUser)) {
            Thread reconnectionTimer = reconnectionTimers.remove(lobbyUser);
            reconnectionTimer.interrupt();
        }

        if(victoryByDefaultTimer!=null) {
            victoryByDefaultTimer.interrupt();
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
        //Count how many players are left online
        int onlineCounter = 0;
        for(LobbyUser user : lobbyUsers.values())
            if(user.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE))
                onlineCounter++;
        logger.fine(onlineCounter+" left online");

        //If the game has not started then the disconnection need to only be handled on the lobby level with the timer
        if(!gameStarted) {
            logger.info("Disconnection method for "+username+": Standard Lobby disconnection");
            timeOutDisconnection(lobbyUser);
        }
        else{
            logger.fine("Game detected as started to looking for game-friendly disconnection methods for "+username);
            //If the game has started and it is now in a phase that expects disconnected players to be removed then
            //activate the disconnection procedure in the game and start the timer thread, the thread will remove the
            //player both from the lobby and from the game.
            if(game.shouldRemovePlayerOnDisconnect()){
                logger.info("Disconnection method for "+username+": Standard Game disconnection");
                game.userDisconnectionProcedure(username);
                timeOutDisconnection(lobbyUser);
            }

            //If all users except one are offline then start the timer thread to mark as winner the remaining player.
            else if(onlineCounter == 1){
                logger.info("Disconnection method for "+username+": Lobby disconnection + VictoryByDefaultTimer");
                game.userDisconnectionProcedure(username);
                startVictoryByDefaultTimer();
            }

            //If the game has started and it is in a phase that does not allow for player removal and there are more than one
            //players left online then you DON'T remove the player from the lobby and only start the user disconnection procedure in the game.

            else {
                logger.info("Disconnection method for "+username+": Disconnection without removal");
                game.userDisconnectionProcedure(username);
            }
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

        //todo make quit similar to disconnection with the various cases

        System.out.println("User "+username+" has quit from the lobby");
        if(gameStarted)
            game.quit(username);
        removeUser(lobbyUser);
    }

    private void timeOutDisconnection(LobbyUser lobbyUser){

        String username = lobbyUser.getUsername();

        Thread reconnectionTimer = new Thread(() -> {
            logger.info("Disconnection Timer Thread started for user "+username);
            System.out.println("Disconnection Timer Thread started for user "+username);
            try {
                //todo add timer sleep time to config
                Thread.sleep(30000);
                logger.info("Disconnection timer is over for "+username);
                removeUser(lobbyUser);

                if(gameStarted)
                    game.removePlayer(username);

                reconnectionTimers.remove(lobbyUser);
            }
            catch (InterruptedException e) {
                System.out.println("User " + username + " has reconnected before the timeout, they will not be removed from lobby");
            }
        });
        reconnectionTimers.put(lobbyUser, reconnectionTimer);
        reconnectionTimer.setDaemon(true);
        reconnectionTimer.start();
    }

    private void startVictoryByDefaultTimer(){
        victoryByDefaultTimer = new Thread(() -> {

            logger.info("Victory by default timer started in lobby "+lobbyName);

            try{
                Thread.sleep(30000);
                logger.info("Victory by default timer is over in lobby "+lobbyName);

                List<LobbyUser> lobbyUserList = lobbyUsers.values().stream().toList();

                logger.fine("Removing each offline user from lobby and game");
                for(int i = 0; i < lobbyUserList.size(); i++){
                    LobbyUser lobbyUser = lobbyUserList.get(i);
                    if(!lobbyUser.getConnectionStatus().equals(LobbyUserConnectionStates.ONLINE)){
                        logger.info("Removing user "+i+": "+lobbyUser.getUsername());
                        removeUser(lobbyUser);
                        //No need to decrement counter i after removal as the removal is on the map, the list
                        //over which we iterate does not change after remove, only the map.
                    }
                }

                logger.fine("Starting game over procedure");
                game.gameOver();
            }
            catch (InterruptedException e) {
                logger.info("Victory By default timer stopped.");
            }
        });

        victoryByDefaultTimer.setDaemon(true);
        victoryByDefaultTimer.start();
    }

    private void removeUser(LobbyUser lobbyUser){
        logger.info("Removing user "+lobbyUser.getUsername()+" from lobby "+lobbyName);
        synchronized (usersLock) {
            lobbyUsers.remove(lobbyUser.getUsername());
            availableUserColors.add(lobbyUser.getColor());
            lobbyObserver.unsubscribe(lobbyUser.getUsername());

            if(lobbyUser.getLobbyRole().equals(LobbyRoles.ADMIN)) {
                Random random = new Random();
                List<LobbyUser> lobbyUsersList = lobbyUsers.values().stream().toList();
                int nextAdminIndex = random.nextInt(lobbyUsersList.size()-1);
                lobbyUsersList.get(nextAdminIndex).setAdmin();
            }
        }
        logger.fine("Lobby user "+lobbyUser.getUsername()+" removed from lobby "+lobbyName);

        //If a user leaves the lobby while the game has not yet started then the lobby becomes join-able again
        //to meet the required max players
        if(!gameStarted)
            lobbyClosed = false;
        else {
            logger.info("Removing user "+lobbyUser.getUsername()+" from Game in lobby "+lobbyName);
            game.removePlayer(lobbyUser.getUsername());
        }


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





    //LOBBY METHODS
    /**
     * Starts the game in the lobby.
     */
    public void startGame(String username) throws AdminRoleRequiredException, InvalidLobbySizeToStartGameException {

        if(!lobbyUsers.get(username).getLobbyRole().equals(LobbyRoles.ADMIN))
            throw new AdminRoleRequiredException("You need to be an admin to start the game in this lobby");

        if(lobbyUsers.values().stream().toList().size() < 2)
            throw new InvalidLobbySizeToStartGameException("You need at least two players in your lobby to start a game.");

        game = GameLoader.startNewGame(lobbyUsers.values().stream().toList(), this, lobbyObserver);
        updateGameController(new GameController(game));

        System.out.println("Game started in lobby: "+lobbyName);

        gameStarted = true;
        lobbyClosed = true;

        toLobbyPreview();
    }

    //todo
    public void gameOver(){
        updateGameController(null);
        game = null;
        gameStarted = false;

        if(lobbyUsers.size() < lobbySize)
            lobbyClosed = false;

        System.out.println("Game finished in lobby: "+lobbyName);
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

    private LobbyPreviewRecord toLobbyPreview(){
        return new LobbyPreviewRecord(lobbyName, lobbyUsers.size(), lobbySize, gameStarted);
    }

    public void addGameControllerObserver(String username, GameControllerObserver observer){
        gameControllerObservers.put(username, observer);
    }

    private void removeGameControllerObserver(String username){
        gameControllerObservers.remove(username);
    }





    //TO RECORD METHODS
    private LobbyRecord toLobbyRecord(){
        return new LobbyRecord(lobbyName, lobbySize, availableUserColors);
    }

    private List<LobbyUserRecord> toLobbyUserRecord(){
        List<LobbyUserRecord> lobbyUserRecords = new ArrayList<>();
        for(LobbyUser lobbyUser : lobbyUsers.values())
            lobbyUserRecords.add(lobbyUser.toRecord());

        return lobbyUserRecords;
    }
}
