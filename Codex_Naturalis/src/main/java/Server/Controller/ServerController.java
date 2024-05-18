package Server.Controller;

import Exceptions.Server.LobbyNameAlreadyTakenException;
import Exceptions.Server.LobbyNotFoundException;
import Exceptions.Server.LogInExceptions.AccountAlreadyExistsException;
import Exceptions.Server.LogInExceptions.AccountAlreadyLoggedInException;
import Exceptions.Server.LogInExceptions.AccountNotFoundException;
import Exceptions.Server.LogInExceptions.IncorrectPasswordException;
import Exceptions.Server.LobbyExceptions.InvalidLobbySettingsException;
import Exceptions.Server.LobbyExceptions.LobbyClosedException;
import Exceptions.Server.LobbyExceptions.LobbyUserAlreadyConnectedException;
import Server.Model.ServerModel;
import Server.Network.ClientHandler.ClientHandler;
import Server.ServerMain;

import java.util.logging.Logger;

/**
 * The ServerController class serves as the controller for the server-side model.
 * It handles operations related to server functionalities such as creating and joining lobbies.
 */
public class ServerController {
    //ATTRIBUTES
    private final ServerModel model;




    //CONSTRUCTOR
    /**
     * Constructs a ServerController object with the specified server model.
     *
     * @param model The ServerModel object associated with this controller.
     */
    public ServerController(ServerModel model){
        Logger logger = Logger.getLogger(ServerMain.class.getName());
        logger.fine("Initializing ServerModel Controller");
        this.model = model;
        logger.info("Server Model Controller created");
    }





    //CONTROLLER METHODS
    public String signUp(ClientHandler ch, String username, String password) throws AccountAlreadyExistsException {
        return model.signUp(ch, username, password);
    }

    public String login(ClientHandler ch, String username, String password) throws IncorrectPasswordException, AccountAlreadyLoggedInException, AccountNotFoundException {
        return model.login(ch, username, password);
    }

    public void userDisconnectionProcedure(String username){
         model.userDisconnectionProcedure(username);
    }

    public void quitLayer(String username){
        model.quit(username);
    }

    /**
     * Creates a new lobby with the given name and assigns the specified user as the owner.
     *
     * @param lobbyName The name of the lobby to be created.
     * @param targetNumberUsers Number of players the admin wants to start a game with.
     * @return The LobbyController object associated with the newly created lobby.
     */
    public LobbyController createNewLobby(String lobbyName, String username, int targetNumberUsers, ClientHandler ch) throws LobbyNameAlreadyTakenException, InvalidLobbySettingsException {
        return model.createNewLobby(lobbyName, username, targetNumberUsers, ch);
    }

    /**
     * Allows a user to join an existing lobby with the given lobbyName.<br>
     * Adds the user to the lobby and returns the controller for that lobby.
     *
     * @param lobbyName The lobbyName of the lobby to join.
     * @return The LobbyController object associated with the joined lobby.
     */
    public LobbyController joinLobby(String lobbyName, String username, ClientHandler connection) throws LobbyNotFoundException, LobbyClosedException, LobbyUserAlreadyConnectedException {
        return model.joinLobby(lobbyName, username, connection);
    }





    //OBSERVER METHODS
    public void addLobbyPreviewObserver(String username, ClientHandler ch){
        model.addLobbyPreviewObserver(username, ch);
    }

    public void removeLobbyPreviewObserver(String username){
        model.removeLobbyPreviewObserver(username);
    }
}
