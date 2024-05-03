package Server.Model;

import Network.ServerClientPacket.SCPPrintPlaceholder;
import Server.Controller.LobbyController;
import Server.Exceptions.*;
import Server.Interfaces.LayerUser;
import Server.Interfaces.ServerModelLayer;
import Server.Model.Lobby.Exceptions.InvalidLobbySettingsException;
import Server.Model.Lobby.Exceptions.LobbyClosedException;
import Server.Model.Lobby.Exceptions.LobbyUserAlreadyConnectedException;
import Server.Model.Lobby.Lobby;
import Server.Model.Lobby.LobbyPreview;
import Server.Network.ClientHandler.ClientHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the model of the server, managing user accounts, authentication, and lobbies.
 */
public class ServerModel implements ServerModelLayer {
    //ATTRIBUTES
    private final Map<String, ServerUserInfo> userDatabase;
    private final Map<String, ClientHandler> userConnections;
    private final Map<ClientHandler, String> connectionsUser;
    private final Map<String, LobbyController> lobbiesMap;

    private final Map<String, LobbyPreview> lobbyPreviewMap;
    private final LobbyPreviewObserverRelay lobbyPreviewObserverRelay;





    //CONSTRUCTOR

    /**
     * Default Constructor
     */
    public ServerModel(){
        System.out.println("Initializing ServerMain Model");
        userDatabase = new HashMap<>();
        userConnections = new HashMap<>();
        connectionsUser = new HashMap<>();
        lobbiesMap = new HashMap<>();
        lobbyPreviewMap = new HashMap<>();
        lobbyPreviewObserverRelay = new LobbyPreviewObserverRelay(lobbyPreviewMap);
    }




    //ACCOUNT COMMANDS
    /**
     * Registers a new user with the server.
     *
     * @param ch       The client handler associated with the user.
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @return The ServerUser object representing the registered user.
     * @throws AccountAlreadyExistsException If the account already exists.
     */
    public ServerUser signUp(ClientHandler ch, String username, String password) throws AccountAlreadyExistsException {
        if(userDatabase.containsKey(username)) {
            throw new AccountAlreadyExistsException(ch, username, "");
        }

        String salt = ServerSecurity.generateSalt();
        String hashedPassword = ServerSecurity.hashPassword(password, salt);

        ServerUser serverUser = new ServerUser(username);
        ServerUserInfo serverUserInfo = new ServerUserInfo(serverUser, hashedPassword, salt);


        //Add new user to user database
        userDatabase.put(serverUser.getUsername(), serverUserInfo);

        //Link user to their current session connection
        userConnections.put(serverUser.getUsername(), ch);
        connectionsUser.put(ch, serverUser.getUsername());
        serverUser.setOnline();

        System.out.println("Sign up successful for user: "+username);

        ch.sendPacket(new SCPPrintPlaceholder("Sign-up successful"));

        return serverUser;
    }

    /**
     * Authenticates a user with the server.
     *
     * @param ch       The client handler associated with the user.
     * @param username The username of the user trying to log in.
     * @param password The password of the user trying to log in.
     * @return The ServerUser object representing the authenticated user.
     * @throws AccountNotFoundException      If the account is not found.
     * @throws AccountAlreadyLoggedInException If the account is already logged in.
     * @throws IncorrectPasswordException    If the password is incorrect.
     */
    public ServerUser login(ClientHandler ch, String username, String password) throws AccountNotFoundException, AccountAlreadyLoggedInException, IncorrectPasswordException {
        if(!userDatabase.containsKey(username)){
            throw new AccountNotFoundException(ch, username, "");
        }

        //Check if that user already has a connection associated with them right now
        if(userConnections.containsKey(username)){
            throw new AccountAlreadyLoggedInException(ch, username, "");
        }

        //Get serverUser info by username
        ServerUserInfo serverUserInfo = userDatabase.get(username);

        //Get user from userInfo by providing correct password
        ServerUser serverUser = serverUserInfo.getUser(password);

        //Add the current connection to the account
        userConnections.put(serverUser.getUsername(), ch);
        connectionsUser.put(ch, serverUser.getUsername());
        serverUser.setOnline();
        System.out.println("serverUser "+username+" logged in.");

        ch.sendPacket(new SCPPrintPlaceholder("Log-in successful"));

        return serverUser;
    }





    //LOBBY COMMANDS
    /**
     * Creates a new lobby.
     *
     * @param lobbyName  The name of the new lobby.
     * @param targetNumberUsers Number of players the admin wants to start a game with.
     * @param serverUser The user creating the lobby.
     * @param connection The client handler associated with the user.
     * @return The LobbyController object representing the new lobby.
     * @throws LobbyNameAlreadyTakenException If the lobby name is already taken.
     */
    public LobbyController createNewLobby(String lobbyName, int targetNumberUsers, ServerUser serverUser, ClientHandler connection) throws LobbyNameAlreadyTakenException, InvalidLobbySettingsException {
        if(lobbiesMap.containsKey(lobbyName)){
            throw new LobbyNameAlreadyTakenException(connection, serverUser.getUsername(), "");
        }

        System.out.println("Creating new Lobby");
        Lobby lobby = new Lobby(lobbyName, targetNumberUsers, serverUser, connection, lobbyPreviewObserverRelay);

        LobbyController lobbyController = new LobbyController(lobby);
        lobbiesMap.put(lobby.getLobbyName(), lobbyController);
        lobbyPreviewMap.put(lobby.getLobbyName(), lobby.getLobbyPreview());

        System.out.println("User "+serverUser.getUsername()+" created lobby: "+lobbyName);

        return lobbyController;
    }

    /**
     * Joins an existing lobby.
     *
     * @param lobbyName  The name of the lobby to join.
     * @param serverUser The user joining the lobby.
     * @param connection The client handler associated with the user.
     * @return The LobbyController object representing the joined lobby.
     * @throws LobbyNotFoundException If the lobby is not found.
     */
    public LobbyController joinLobby(String lobbyName, ServerUser serverUser, ClientHandler connection) throws LobbyNotFoundException, LobbyClosedException, LobbyUserAlreadyConnectedException {
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
     * @param user user to disconnect.
     */
    @Override
    public void userDisconnectionProcedure(LayerUser user) {
        logOut(user);
    }

    /**
     * Handles the user log-out process.
     *
     * @param user user to log-out.
     */
    @Override
    public void logOut(LayerUser user) {
        ServerUser serverUser = (ServerUser) user;
        serverUser.setOffline();
        ClientHandler connection = userConnections.remove(serverUser.getUsername());
        connectionsUser.remove(connection);
        removeLobbyPreviewObserver(connection);
        System.out.println("ServerUser: "+ serverUser.getUsername() + " successfully logged-out of Server");
    }





    //OBSERVER METHODS
    public void addLobbyPreviewObserver(ClientHandler ch){
        this.lobbyPreviewObserverRelay.addObserver(ch);
    }

    public void removeLobbyPreviewObserver(ClientHandler ch){
        this.lobbyPreviewObserverRelay.removeObserver(ch);
    }
}
