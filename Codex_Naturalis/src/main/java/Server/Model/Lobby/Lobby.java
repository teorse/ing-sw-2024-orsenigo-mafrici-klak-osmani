package Server.Model.Lobby;

import Network.ServerClientPacket.SCPPrintPlaceholder;
import Network.ServerClientPacket.ServerClientPacket;
import Server.Controller.GameController;
import Server.Controller.InputHandler.LobbyInputHandler;
import Server.Interfaces.LayerUser;
import Server.Interfaces.ServerModelLayer;
import Server.Model.Lobby.Exceptions.LobbyClosedException;
import Server.Model.Lobby.Exceptions.InvalidLobbySettingsException;
import Server.Model.Lobby.Exceptions.LobbyUserAlreadyConnectedException;
import Server.Model.Lobby.GameDemo.Game;
import Server.Model.LobbyPreviewObserverRelay;
import Server.Model.ServerUser;
import Server.Network.ClientHandler.ClientHandler;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a lobby in the server, managing user connections, game control, and messaging within the lobby.
 */
public class Lobby implements ServerModelLayer {
    //ATTRIBUTES
    private final String lobbyName;
    private final int targetNumberUsers;



    //Attributes that should always be synchronized to usersLock
    private final List<LobbyUser> lobbyUsers;
    private final Map<ServerUser, LobbyUser> serverUserToLobbyUser;
    private final Map<LobbyUser, ServerUser> lobbyUserToServerUser;
    private final Map<LobbyUser, ClientHandler> lobbyUserConnection;
    //the lobby is set to closed when the game starts or when the max player amount is reached.
    private boolean lobbyClosed;
    //Lock for this group of Attributes
    private final Object usersLock = new Object();



    private final LobbyPreview preview;

    private GameController gameController;
    private Model.Game.Game game;
    private boolean gameStarted;


    //LISTENERS
    private final List<LobbyInputHandler> gameControllerObservers;
    private final PropertyChangeSupport lobbyUsersChange;

    //DISCONNECTION TIMER
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
        this.lobbyUsers = new ArrayList<>();
        lobbyUserConnection = new HashMap<>();
        serverUserToLobbyUser = new HashMap<>();
        lobbyUserToServerUser = new HashMap<>();
        gameControllerObservers = new ArrayList<>();
        gameStarted = false;
        lobbyClosed = false;

        //Listens to changes in user connection stats and updates the views.
        LobbyListener lobbyListener = new LobbyListener(this);
        lobbyUsersChange = new PropertyChangeSupport(this);
        lobbyUsersChange.addPropertyChangeListener(lobbyListener);


        reconnectionTimers = new HashMap<>();

        preview = new LobbyPreview(lobbyName);
        preview.addObserver(lobbyPreviewObserverRelay);

        LobbyUser lobbyUser = new LobbyUser(serverUser, LobbyRoles.ADMIN);
        addLobbyUserToLobby(serverUser, ch, lobbyUser);

        sendPacket(lobbyUser, new SCPPrintPlaceholder("You have started the lobby "+lobbyName));
    }

    private void addLobbyUserToLobby(ServerUser serverUser, ClientHandler ch, LobbyUser lobbyUser){

        synchronized (usersLock) {

            //Links the server and lobby layers of the user. The link will be used during the re-connection phase.
            serverUserToLobbyUser.put(serverUser, lobbyUser);
            lobbyUserToServerUser.put(lobbyUser, serverUser);
            lobbyUsers.add(lobbyUser);

            if (lobbyUsers.size() == targetNumberUsers)
                lobbyClosed = true;

            //Links the lobby layer with the output channel of the user to be able to directly send messages to the
            //user from this layer without needing to go up to the server level and occupy that level's resources.
            lobbyUserConnection.put(lobbyUser, ch);

            preview.setUsers(lobbyUsers.size());
        }

        lobbyUsersChange.firePropertyChange("LobbyUsersChange", null, lobbyUsers);
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
            if (serverUserToLobbyUser.containsKey(serverUser)) {
                reconnect(serverUser, ch);
                return;
            }
        }

        //If the game had already started then throw exception
        if(lobbyClosed){
            throw new LobbyClosedException("Can't join lobby, the lobby is closed.");
        }

        //If none of the above scenarios were true then proceed to add new user to lobby
        LobbyUser lobbyUser = new LobbyUser(serverUser, LobbyRoles.GUEST);

        addLobbyUserToLobby(serverUser, ch, lobbyUser);

        sendPacket(lobbyUser, new SCPPrintPlaceholder("You have joined lobby: "+lobbyName));
    }

    private void reconnect(ServerUser serverUser, ClientHandler ch) throws LobbyUserAlreadyConnectedException {
        LobbyUser lobbyUser = serverUserToLobbyUser.get(serverUser);

        //If reconnecting to user that is not actually DISCONNECTED throw exception
        if(!lobbyUser.getConnectionToLobby().equals("DISCONNECTED")){
            throw new LobbyUserAlreadyConnectedException("user "+lobbyUser.getUsername()+" is already connected to "+lobbyName);
        }

        //Interrupt the disconnection timer
        Thread reconnectionTimer = reconnectionTimers.remove(lobbyUser);
        reconnectionTimer.interrupt();

        //Update with new connection source and set them back online
        lobbyUserConnection.put(lobbyUser, ch);
        lobbyUser.setOnline();

        //notify of user status change
        lobbyUsersChange.firePropertyChange("LobbyUsersChange", null, lobbyUsers);
    }





    //QUITTING METHODS
    /**
     * Handles the abrupt disconnection procedure for a user.
     *
     * @param user The user disconnecting.
     */
    @Override
    public void userDisconnectionProcedure(LayerUser user) {

        LobbyUser lobbyUser = (LobbyUser) user;

        String username = lobbyUser.getUsername();

        System.out.println("User "+username+" has disconnected from server");
        lobbyUser.setOffline();
        synchronized (usersLock) {
            lobbyUserConnection.remove(lobbyUser);
        }

        lobbyUsersChange.firePropertyChange("LobbyUsersChange", null, lobbyUsers);

        //If the game has not started then the disconnection need to only be handled on the lobby level with the timer
        if(!gameStarted) {
            timeOutDisconnection(lobbyUser);
        }
        else{
            //If the game has started and it is now in a phase that expects disconnected players to be removed then
            //activate the disconnection procedure in the game and start the timer thread, the thread will remove the
            //player both from the lobby and from the game.
            if(game.shouldRemovePlayerOnDisconnect()){
                game.userDisconnectionProcedure(lobbyUser);
                timeOutDisconnection(lobbyUser);
            }
            //If the game has started and it is in a phase that does not allow for player removal then you DON'T
            //remove the player from the lobby and only start the user disconnection procedure in the game.
            else
                game.userDisconnectionProcedure(lobbyUser);
        }
    }

    /**
     * Allows a user to quit the lobby.
     *
     * @param user The user quitting the lobby.
     */
    @Override
    public void quit(LayerUser user){

        LobbyUser lobbyUser = (LobbyUser) user;
        String username = lobbyUser.getUsername();

        System.out.println("User "+username+" has quit from the lobby");
        if(gameController != null)
            gameController.quitGame(lobbyUser);
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
                    game.removePlayer(lobbyUser);

                gameController.quitGame(lobbyUser);

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

            lobbyUsers.remove(lobbyUser);

            ServerUser serverUser = lobbyUserToServerUser.remove(lobbyUser);
            serverUserToLobbyUser.remove(serverUser);

            preview.setUsers(lobbyUsers.size());
        }

        //If a user leaves the lobby while the game has not yet started then the lobby becomes joinable again
        //to meet the required max players
        if(!gameStarted)
            lobbyClosed = false;

        lobbyUsersChange.firePropertyChange("LobbyUsersChange", null, lobbyUsers);
    }





    //LOBBY METHODS
    /**
     * Starts the game in the lobby.
     */
    public void startGame(){
        Game game = new Game(lobbyUsers, this);
        updateGameController(new GameController(game));

        System.out.println("Game started in lobby: "+lobbyName);

        broadcastPacket(new SCPPrintPlaceholder("Game has started"));

        gameStarted = true;
        lobbyClosed = true;

        preview.setGameStarted(true);
    }





    //GETTERS
    public String getLobbyName(){
        return lobbyName;
    }

    protected List<LobbyUser> getUsers(){
        return lobbyUsers;
    }

    public GameController getGameController(){
        return gameController;
    }

    public LobbyUser getLobbyUserByServerUser(LayerUser user){
        ServerUser serverUser = (ServerUser) user;
        return serverUserToLobbyUser.get(serverUser);
    }

    public LobbyPreview getLobbyPreview(){
        return this.preview;
    }





    //NETWORKING METHODS
    /**
     * Sends a packet to a specific user in the lobby.
     *
     * @param lobbyUser The lobby user to whom the message will be sent.
     * @param message   The message to send.
     */
    public void sendPacket(LobbyUser lobbyUser, ServerClientPacket message){

        if(lobbyUser.getConnectionToLobby().equals("CONNECTED")) {
            ClientHandler ch = lobbyUserConnection.get(lobbyUser);
            ch.sendPacket(message);
        }
        else{
            System.out.println("Message could not be sent as user is disconnected");
        }
    }

    /**
     * Broadcasts a packet to all users in the lobby.
     *
     * @param message The message to broadcast.
     */
    public void broadcastPacket(ServerClientPacket message){
        for(LobbyUser lobbyUser : lobbyUsers){
            if(lobbyUser.getConnectionToLobby().equals("CONNECTED")) {
                ClientHandler ch = lobbyUserConnection.get(lobbyUser);
                ch.sendPacket(message);
            }
        }
    }





    //DEMO METHODS
    /**
     * Executes command 1 as part of the lobby's demo functionality.
     */
    public void Command1(){
        System.out.println("Command 1 executed in Lobby: "+lobbyName);
    }

    /**
     * Executes command 2 as part of the lobby's demo functionality.
     */
    public void Command2(){
        System.out.println("Command 2 executed in Lobby: "+lobbyName);
    }

    /**
     * Executes command 3 as part of the lobby's demo functionality.
     */
    public void Command3(){
        System.out.println("Command 3 executed in Lobby: "+lobbyName);
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

    public void addGameControllerObserver(LobbyInputHandler observer){
        gameControllerObservers.add(observer);
    }

    public void removeGameControllerObserver(LobbyInputHandler observer){
        gameControllerObservers.remove(observer);
    }
}
