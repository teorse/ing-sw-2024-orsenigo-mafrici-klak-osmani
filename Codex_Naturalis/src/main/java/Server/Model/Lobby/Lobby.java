package Server.Model.Lobby;

import Client.Model.Records.LobbyPreviewRecord;
import Exceptions.Server.LobbyExceptions.UnavailableLobbyUserColorException;
import Model.Game.Game;
import Network.ServerClientPacket.Demo.SCPUpdateLobbyUsers;
import Server.Model.Lobby.Game.GameLoader;
import Network.ServerClientPacket.Demo.SCPPrintPlaceholder;
import Server.Controller.GameController;
import Server.Controller.InputHandler.LobbyInputHandler;
import Server.Interfaces.ServerModelLayer;
import Exceptions.Server.LobbyExceptions.LobbyClosedException;
import Exceptions.Server.LobbyExceptions.InvalidLobbySettingsException;
import Exceptions.Server.LobbyExceptions.LobbyUserAlreadyConnectedException;
import Server.Model.LobbyPreviewObserverRelay;
import Server.Model.ServerUser;
import Server.Network.ClientHandler.ClientHandler;

import java.util.*;

/**
 * This class represents a lobby in the server, managing user connections, game control, and messaging within the lobby.
 */
public class Lobby implements ServerModelLayer {
    //ATTRIBUTES
    private final String lobbyName;
    private final int targetNumberUsers;


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
    private final List<LobbyInputHandler> gameControllerObservers;
    private final LobbyPreviewObserverRelay lobbyPreviewObserverRelay;
    private final ObserverRelay lobbyObserver;

    //DISCONNECTION TIMER
    /**
     * Map that stores for each disconnected player their thread containing the count-down for their removal from the lobby
     */
    private final Map<LobbyUser, Thread> reconnectionTimers;





    //CONSTRUCTOR

    /**
     * Default constructor, created the lobby and adds the admin user.
     * @param lobbyName         Name of the lobby
     * @param targetNumberUsers Number of players the admin wants to start a game with.
     * @param serverUser        Admin/creator of the lobby
     * @param ch                Connection associated with the user
     */
    public Lobby(String lobbyName, int targetNumberUsers, ServerUser serverUser, ClientHandler ch, LobbyPreviewObserverRelay lobbyPreviewObserverRelay) throws InvalidLobbySettingsException {

        if(targetNumberUsers > 4 || targetNumberUsers < 2)
            throw new InvalidLobbySettingsException("The value provided for max lobby users is not a valid number.\n" +
                    "Please provide a value between 2 and 4");

        this.targetNumberUsers = targetNumberUsers;
        this.lobbyName = lobbyName;
        this.lobbyUsers = new HashMap<>();
        this.lobbyPreviewObserverRelay = lobbyPreviewObserverRelay;
        lobbyObserver = new ObserverRelay();

        availableUserColors = new ArrayList<>(){{
           add(LobbyUserColors.RED);
           add(LobbyUserColors.BLUE);
           add(LobbyUserColors.GREEN);
           add(LobbyUserColors.YELLOW);
        }};

        gameControllerObservers = new ArrayList<>();
        gameStarted = false;
        lobbyClosed = false;


        reconnectionTimers = new HashMap<>();

        LobbyUser lobbyUser = new LobbyUser(serverUser, LobbyRoles.ADMIN);
        addLobbyUserToLobby(ch, lobbyUser);

        lobbyObserver.update(lobbyUser.getUsername(), new SCPPrintPlaceholder("You have started the lobby "+lobbyName));
    }

    private void addLobbyUserToLobby(ClientHandler ch, LobbyUser lobbyUser){

        synchronized (usersLock) {

            //Links the connection to the lobby user. The link will be used during the re-connection phase.
            String username = lobbyUser.getUsername();
            lobbyUsers.put(username, lobbyUser);
            lobbyObserver.subscribe(username, ch);

            if (lobbyUsers.size() == targetNumberUsers)
                lobbyClosed = true;
        }

        //Pick random color for the user
        synchronized (colorsLock) {
            Random rand = new Random();
            int random = rand.nextInt(0, availableUserColors.size());
            lobbyUser.setColor(availableUserColors.remove(random));
        }

        //todo
        updateLobbyPreview();
        lobbyObserver.update(new SCPUpdateLobbyUsers(getUsers()));
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
                return;
            }
        }

        //If the game had already started then throw exception
        if(lobbyClosed){
            throw new LobbyClosedException("Can't join lobby, the lobby is closed.");
        }

        //If none of the above scenarios were true then proceed to add new user to lobby
        LobbyUser lobbyUser = new LobbyUser(serverUser, LobbyRoles.GUEST);

        addLobbyUserToLobby(ch, lobbyUser);

        lobbyObserver.update(lobbyUser.getUsername(), new SCPPrintPlaceholder("You have joined lobby: "+lobbyName));
    }

    private void reconnect(LobbyUser lobbyUser, ClientHandler ch) throws LobbyUserAlreadyConnectedException {

        //If reconnecting to user that is not actually DISCONNECTED throw exception
        if(!lobbyUser.getConnectionStatus().equals(LobbyUserConnectionStates.OFFLINE)){
            throw new LobbyUserAlreadyConnectedException("user "+lobbyUser.getUsername()+" is already connected to "+lobbyName);
        }

        //Interrupt the disconnection timer
        Thread reconnectionTimer = reconnectionTimers.remove(lobbyUser);
        reconnectionTimer.interrupt();

        //Update with new connection source and set them back online
        lobbyObserver.subscribe(lobbyUser.getUsername(), ch);
        lobbyUser.setOnline();

        //notify of user status change
        lobbyObserver.update(new SCPUpdateLobbyUsers(getUsers()));
    }





    //QUITTING METHODS
    /**
     * Handles the abrupt disconnection procedure for a user.
     *
     * @param username The user disconnecting.
     */
    @Override
    public void userDisconnectionProcedure(String username) {

        LobbyUser lobbyUser = lobbyUsers.get(username);

        System.out.println("User "+username+" has disconnected from server");
        lobbyUser.setOffline();
        synchronized (usersLock) {
            lobbyObserver.unsubscribe(username);
        }

        lobbyObserver.update(new SCPUpdateLobbyUsers(getUsers()));

        //If the game has not started then the disconnection need to only be handled on the lobby level with the timer
        if(!gameStarted) {
            timeOutDisconnection(lobbyUser);
        }
        else{
            //If the game has started and it is now in a phase that expects disconnected players to be removed then
            //activate the disconnection procedure in the game and start the timer thread, the thread will remove the
            //player both from the lobby and from the game.
            if(game.shouldRemovePlayerOnDisconnect()){
                game.userDisconnectionProcedure(username);
                timeOutDisconnection(lobbyUser);
            }
            //If the game has started and it is in a phase that does not allow for player removal then you DON'T
            //remove the player from the lobby and only start the user disconnection procedure in the game.
            else
                game.userDisconnectionProcedure(username);
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

        System.out.println("User "+username+" has quit from the lobby");
        if(gameStarted)
            game.quit(username);
        removeUser(lobbyUser);
    }

    private void timeOutDisconnection(LobbyUser lobbyUser){

        String username = lobbyUser.getUsername();

        Thread reconnectionTimer = new Thread(() -> {
            System.out.println("They have 90 seconds to reconnect to the lobby after which they will be removed from lobby");
            try {
                Thread.sleep(90000);
                removeUser(lobbyUser);

                if(gameStarted)
                    game.removePlayer(username);

                reconnectionTimers.remove(lobbyUser);
                System.out.println("User " + username + " has been removed from lobby after disconnection timeout");
            }
            catch (InterruptedException e) {
                System.out.println("User " + username + " has reconnected before the timeout, they will not be removed from lobby");
            }
        });
        reconnectionTimers.put(lobbyUser, reconnectionTimer);
        reconnectionTimer.setDaemon(true);
        reconnectionTimer.start();
    }

    private void removeUser(LobbyUser lobbyUser){

        synchronized (usersLock) {
            lobbyUsers.remove(lobbyUser.getUsername());

            lobbyObserver.unsubscribe(lobbyUser.getUsername());

            updateLobbyPreview();
        }

        //If a user leaves the lobby while the game has not yet started then the lobby becomes join-able again
        //to meet the required max players
        if(!gameStarted)
            lobbyClosed = false;

        lobbyObserver.update(new SCPUpdateLobbyUsers(getUsers()));
    }





    //LOBBY METHODS
    /**
     * Starts the game in the lobby.
     */
    public void startGame(){
        game = GameLoader.startNewGame(lobbyUsers.values().stream().toList(), this, lobbyObserver);
        updateGameController(new GameController(game));

        System.out.println("Game started in lobby: "+lobbyName);

        lobbyObserver.update(new SCPPrintPlaceholder("Game has started"));

        gameStarted = true;
        lobbyClosed = true;

        updateLobbyPreview();
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
        lobbyObserver.update(new SCPUpdateLobbyUsers(getUsers()));
    }





    //GETTERS
    public String getLobbyName(){
        return lobbyName;
    }

    protected List<LobbyUser> getUsers(){
        return lobbyUsers.values().stream().toList();
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
        for(LobbyInputHandler observer : gameControllerObservers){
            observer.updateGameController(newGameController);
        }
    }

    private void updateLobbyPreview(){
        LobbyPreviewRecord preview = new LobbyPreviewRecord(lobbyName, lobbyUsers.size(), targetNumberUsers, gameStarted);
        lobbyPreviewObserverRelay.updateLobbyPreview(preview);
    }

    public void addGameControllerObserver(LobbyInputHandler observer){
        gameControllerObservers.add(observer);
    }

    public void removeGameControllerObserver(LobbyInputHandler observer){
        gameControllerObservers.remove(observer);
    }
}
