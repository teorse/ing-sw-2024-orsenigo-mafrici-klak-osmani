package Server.Model;

import Exceptions.Server.LobbyNameAlreadyTakenException;
import Exceptions.Server.LobbyNotFoundException;
import Network.ServerClient.Demo.SCPPrintPlaceholder;
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

/**
 * This class represents the model of the server, managing user accounts, authentication, and lobbies.
 */
public class ServerModel implements ServerModelLayer {
    //ATTRIBUTES
    private final Map<String, ServerUserInfo> userDatabase;
    private final Map<String, LobbyController> lobbiesMap;

    private final Map<String, ServerUser> loggedInUsers;

    private final LobbyPreviewObserverRelay lobbyPreviewObserverRelay;





    //CONSTRUCTOR

    /**
     * Default Constructor
     */
    public ServerModel(){
        System.out.println("Initializing ServerMain Model");
        userDatabase = new HashMap<>();
        lobbiesMap = new HashMap<>();
        loggedInUsers = new HashMap<>();
        lobbyPreviewObserverRelay = new LobbyPreviewObserverRelay();
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
        if(userDatabase.containsKey(username)) {
            throw new AccountAlreadyExistsException(ch, username, "");
        }

        String salt = ServerSecurity.generateSalt();
        String hashedPassword = ServerSecurity.hashPassword(password, salt);

        ServerUser serverUser = new ServerUser(username);
        ServerUserInfo serverUserInfo = new ServerUserInfo(serverUser, hashedPassword, salt);


        //Add new user to user database
        userDatabase.put(serverUser.getUsername(), serverUserInfo);

        //Set user online
        loggedInUsers.put(username, serverUser);

        System.out.println("Sign up successful for user: "+username);

        ch.sendPacket(new SCPPrintPlaceholder("Sign-up successful"));

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
        if(!userDatabase.containsKey(username)){
            throw new AccountNotFoundException(ch, username, "");
        }

        //Check if that user already has a connection associated with them right now
        if(loggedInUsers.containsKey(username)){
            throw new AccountAlreadyLoggedInException(ch, username, "");
        }

        //Get serverUser info by username
        ServerUserInfo serverUserInfo = userDatabase.get(username);

        //Get user from userInfo by providing correct password
        ServerUser serverUser = serverUserInfo.getUser(password);

        loggedInUsers.put(serverUser.getUsername(), serverUser);
        System.out.println("serverUser "+username+" logged in.");

        ch.sendPacket(new SCPPrintPlaceholder("Log-in successful"));

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

        if(lobbiesMap.containsKey(lobbyName)){
            throw new LobbyNameAlreadyTakenException(connection, serverUser.getUsername(), "");
        }

        System.out.println("Creating new Lobby");
        Lobby lobby = new Lobby(lobbyName, targetNumberUsers, serverUser, connection, lobbyPreviewObserverRelay);

        LobbyController lobbyController = new LobbyController(lobby);
        lobbiesMap.put(lobby.getLobbyName(), lobbyController);

        System.out.println("User "+serverUser.getUsername()+" created lobby: "+lobbyName);

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

        if(!lobbiesMap.containsKey(lobbyName))
            throw new LobbyNotFoundException(lobbyName);

        LobbyController lobbyController = lobbiesMap.get(lobbyName);
        lobbyController.joinLobby(serverUser, connection);
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
        removeLobbyPreviewObserver(username);
        loggedInUsers.remove(username);
        System.out.println("User removed from Server");
    }

    /**
     * Handles the user log-out process.
     *
     * @param username user to log-out.
     */
    @Override
    public void quit(String username) {
        userDisconnectionProcedure(username);
        System.out.println("ServerUser: "+ username + " successfully logged-out of Server");
    }





    //OBSERVER METHODS
    public void addLobbyPreviewObserver(String username, ClientHandler ch){
        this.lobbyPreviewObserverRelay.addObserver(username, ch);
    }

    public void removeLobbyPreviewObserver(String username){
        this.lobbyPreviewObserverRelay.removeObserver(username);
    }
}
