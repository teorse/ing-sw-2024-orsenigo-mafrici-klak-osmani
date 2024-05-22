package Server.Model;

import Exceptions.Server.LobbyNameAlreadyTakenException;
import Exceptions.Server.LobbyNotFoundException;
import Server.Controller.LobbyController;
import Exceptions.Server.LogInExceptions.AccountAlreadyExistsException;
import Exceptions.Server.LogInExceptions.AccountAlreadyLoggedInException;
import Exceptions.Server.LogInExceptions.AccountNotFoundException;
import Exceptions.Server.LogInExceptions.IncorrectPasswordException;
import Server.Interfaces.ServerModelLayer;
import Exceptions.Server.LobbyExceptions.InvalidLobbySettingsException;
import Exceptions.Server.LobbyExceptions.LobbyClosedException;
import Exceptions.Server.LobbyExceptions.LobbyUserAlreadyConnectedException;
import Server.Model.Lobby.Lobby;
import Server.Network.ClientHandler.ClientHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class represents the model of the server, managing user accounts, authentication, and lobbies.
 */
public class ServerModel implements ServerModelLayer {
    //ATTRIBUTES
    private final Map<String, ServerUserInfo> userDatabase;
    private final Map<String, LobbyController> lobbiesMap;

    private final Map<String, ServerUser> loggedInUsers;

    private final LobbyPreviewObserverRelay lobbyPreviewObserverRelay;

    private final Logger logger;




    //CONSTRUCTOR

    /**
     * Default Constructor
     */
    public ServerModel(){
        logger = Logger.getLogger(ServerModel.class.getName());
        logger.info("Initializing server Model");

        userDatabase = new HashMap<>();
        lobbiesMap = new HashMap<>();
        loggedInUsers = new HashMap<>();
        lobbyPreviewObserverRelay = new LobbyPreviewObserverRelay();
        logger.fine("Server Model initialized");
    }




    //ACCOUNT COMMANDS
    /**
     * Registers a new user with the server.
     *
     * @param ch       The client handler associated with the user.
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @throws AccountAlreadyExistsException If the account already exists.
     */
    public String signUp(ClientHandler ch, String username, String password) throws AccountAlreadyExistsException {
        logger.fine("Signing connection "+ch+" as user: "+username);

        logger.fine("Checking if username "+username+" is already taken");
        if(userDatabase.containsKey(username)) {
            throw new AccountAlreadyExistsException(ch, username, "");
        }

        String salt = ServerSecurity.generateSalt();
        String hashedPassword = ServerSecurity.hashPassword(password, salt);

        logger.fine("Creating user "+username);
        ServerUser serverUser = new ServerUser(username);
        ServerUserInfo serverUserInfo = new ServerUserInfo(serverUser, hashedPassword, salt);


        //Add new user to user database
        logger.fine("Adding "+username+" to Server database");
        userDatabase.put(serverUser.getUsername(), serverUserInfo);

        //Set user online
        logger.fine("Adding "+username+" to Logged in users map");
        loggedInUsers.put(username, serverUser);

        logger.info("Sign up successful for user: "+username);

        return username;
    }

    /**
     * Authenticates a user with the server.
     *
     * @param ch       The client handler associated with the user.
     * @param username The username of the user trying to log in.
     * @param password The password of the user trying to log in.
     * @throws AccountNotFoundException         If the account is not found.
     * @throws AccountAlreadyLoggedInException  If the account is already logged in.
     * @throws IncorrectPasswordException       If the password is incorrect.
     */
    public String login(ClientHandler ch, String username, String password) throws AccountNotFoundException, AccountAlreadyLoggedInException, IncorrectPasswordException {
        logger.fine("Logging in connection "+ch+" as user: "+username);

        logger.fine("Checking if username "+username+" exists");
        if(!userDatabase.containsKey(username)){
            throw new AccountNotFoundException(ch, username, "");
        }

        //Check if that user already has a connection associated with them right now
        logger.fine("Checking if username "+username+" is already logged in");
        if(loggedInUsers.containsKey(username)){
            throw new AccountAlreadyLoggedInException(ch, username, "");
        }

        //Get serverUser info by username
        logger.fine("Retrieving user information for user "+username);
        ServerUserInfo serverUserInfo = userDatabase.get(username);

        //Get user from userInfo by providing correct password
        logger.fine("Trying provided password for user "+username);
        ServerUser serverUser = serverUserInfo.getUser(password);

        loggedInUsers.put(serverUser.getUsername(), serverUser);
        logger.info("serverUser "+username+" logged in.");

        return serverUser.getUsername();
    }





    //LOBBY COMMANDS
    /**
     * Creates a new lobby.
     *
     * @param lobbyName  The name of the new lobby.
     * @param targetNumberUsers Number of players the admin wants to start a game with.
     * @param connection The client handler associated with the user.
     * @return The LobbyController object representing the new lobby.
     * @throws LobbyNameAlreadyTakenException If the lobby name is already taken.
     */
    public LobbyController createNewLobby(String lobbyName, String username, int targetNumberUsers, ClientHandler connection) throws LobbyNameAlreadyTakenException, InvalidLobbySettingsException {
        ServerUser serverUser = loggedInUsers.get(username);

        logger.fine("Checking if lobby name "+lobbyName+" exists in server");
        if(lobbiesMap.containsKey(lobbyName)){
            throw new LobbyNameAlreadyTakenException(connection, serverUser.getUsername(), "");
        }

        logger.info("User "+username+" is creating Lobby "+lobbyName);
        Lobby lobby = new Lobby(lobbyName, targetNumberUsers, serverUser, connection, this, lobbyPreviewObserverRelay);

        logger.fine("Creating controller for lobby "+lobbyName);
        LobbyController lobbyController = new LobbyController(lobby);
        logger.fine("Adding Lobby "+lobbyName+" to lobby Map");
        lobbiesMap.put(lobby.getLobbyName(), lobbyController);

        logger.info("User "+serverUser.getUsername()+" created lobby: "+lobbyName);

        return lobbyController;
    }

    /**
     * Joins an existing lobby.
     *
     * @param lobbyName  The name of the lobby to join.
     * @param connection The client handler associated with the user.
     * @return The LobbyController object representing the joined lobby.
     * @throws LobbyNotFoundException If the lobby is not found.
     */
    public LobbyController joinLobby(String lobbyName, String username, ClientHandler connection) throws LobbyNotFoundException, LobbyClosedException, LobbyUserAlreadyConnectedException {
        ServerUser serverUser = loggedInUsers.get(username);

        logger.fine("Checking if lobby name "+lobbyName+" exists in server");
        if(!lobbiesMap.containsKey(lobbyName))
            throw new LobbyNotFoundException(lobbyName);

        logger.info("User "+username+" is joining Lobby "+lobbyName);

        logger.fine("Retrieving lobby controller for lobby "+lobbyName);
        LobbyController lobbyController = lobbiesMap.get(lobbyName);
        logger.fine("Attempting to join lobby "+lobbyName);
        lobbyController.joinLobby(serverUser, connection);
        logger.info("User "+username+" joined Lobby "+lobbyName);
        return lobbyController;
    }





    //CLEAN UP METHODS
    /**
     * Handles the user disconnection procedure.
     *
     * @param username user to disconnect.
     */
    @Override
    public void userDisconnectionProcedure(String username) {
        logger.info("Disconnection procedure started in Server Layer for user "+username);
        removeUser(username);
    }

    /**
     * Handles the user log-out process.
     *
     * @param username user to log-out.
     */
    @Override
    public void quit(String username) {
        logger.info("Quitting procedure started in Server Layer for user "+username);
        removeUser(username);
    }

    private void removeUser(String username){
        removeLobbyPreviewObserver(username);
        loggedInUsers.remove(username);
        logger.info("User "+username+" removed from Server Layer");
    }

    public void removeLobby(String lobbyName){
        logger.info("Removing lobby "+lobbyName+" from Server model");
        lobbiesMap.remove(lobbyName);
    }





    //OBSERVER METHODS
    public void addLobbyPreviewObserver(String username, ClientHandler ch){
        logger.fine("Adding observer "+username+ " to lobby previews");
        this.lobbyPreviewObserverRelay.addObserver(username, ch);
    }

    public void removeLobbyPreviewObserver(String username){
        logger.fine("Removing observer "+username+ " from lobby previews");
        this.lobbyPreviewObserverRelay.removeObserver(username);
    }
}
