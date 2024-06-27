package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Exceptions.Server.LobbyNameAlreadyTakenException;
import it.polimi.ingsw.Exceptions.Server.LobbyNotFoundException;
import it.polimi.ingsw.Exceptions.Server.LogInExceptions.AccountAlreadyExistsException;
import it.polimi.ingsw.Exceptions.Server.LogInExceptions.AccountAlreadyLoggedInException;
import it.polimi.ingsw.Exceptions.Server.LogInExceptions.AccountNotFoundException;
import it.polimi.ingsw.Exceptions.Server.LogInExceptions.IncorrectPasswordException;
import it.polimi.ingsw.Exceptions.Server.LobbyExceptions.InvalidLobbySettingsException;
import it.polimi.ingsw.Exceptions.Server.LobbyExceptions.LobbyClosedException;
import it.polimi.ingsw.Exceptions.Server.LobbyExceptions.LobbyUserAlreadyConnectedException;
import it.polimi.ingsw.Server.Model.Server.ServerModel;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;
import it.polimi.ingsw.Server.ServerMain;

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
    /**
     * Signs up a new user with the specified username and password.
     *
     * @param ch       The client handler associated with the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A message indicating the success of the signup process.
     * @throws AccountAlreadyExistsException If an account with the same username already exists.
     */
    public String signUp(ClientHandler ch, String username, String password) throws AccountAlreadyExistsException {
        return model.signUp(ch, username, password);
    }

    /**
     * Logs in a user with the specified username and password.
     *
     * @param ch       The client handler associated with the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A message indicating the success of the login process.
     * @throws IncorrectPasswordException     If the provided password is incorrect.
     * @throws AccountAlreadyLoggedInException If the account is already logged in.
     * @throws AccountNotFoundException       If the account with the specified username does not exist.
     */
    public String login(ClientHandler ch, String username, String password) throws IncorrectPasswordException, AccountAlreadyLoggedInException, AccountNotFoundException {
        return model.login(ch, username, password);
    }

    /**
     * Handles the disconnection of a user.
     *
     * @param username The username of the disconnected user.
     */
    public void userDisconnectionProcedure(String username){
         model.userDisconnectionProcedure(username);
    }

    /**
     * Quits the layer for a user, terminating their session.
     *
     * @param username The username of the user to quit.
     */
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
    /**
     * Adds a client handler as an observer for lobby preview updates.
     *
     * @param username The username of the observer.
     * @param ch       The client handler associated with the observer.
     */
    public void addLobbyPreviewObserver(String username, ClientHandler ch){
        model.addLobbyPreviewObserver(username, ch);
    }

    /**
     * Removes a client handler as an observer for lobby preview updates.
     *
     * @param username The username of the observer to remove.
     */
    public void removeLobbyPreviewObserver(String username){
        model.removeLobbyPreviewObserver(username);
    }
}
