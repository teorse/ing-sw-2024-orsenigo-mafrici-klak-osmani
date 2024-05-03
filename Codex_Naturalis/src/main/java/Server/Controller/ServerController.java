package Server.Controller;

import Server.Exceptions.*;
import Server.Interfaces.LayerUser;
import Server.Model.Lobby.Exceptions.InvalidLobbySettingsException;
import Server.Model.Lobby.Exceptions.LobbyClosedException;
import Server.Model.Lobby.Exceptions.LobbyUserAlreadyConnectedException;
import Server.Model.ServerModel;
import Server.Model.ServerUser;
import Server.Network.ClientHandler.ClientHandler;

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
        this.model = model;
    }





    //CONTROLLER METHODS
    public ServerUser signUp(ClientHandler ch, String username, String password) throws AccountAlreadyExistsException {
        return model.signUp(ch, username, password);
    }

    public ServerUser login(ClientHandler ch, String username, String password) throws IncorrectPasswordException, AccountAlreadyLoggedInException, AccountNotFoundException {
        return model.login(ch, username, password);
    }

    public void userDisconnectionProcedure(LayerUser user){
         model.userDisconnectionProcedure(user);
    }

    public void quitLayer(LayerUser user){
        model.logOut(user);
    }

    /**
     * Creates a new lobby with the given name and assigns the specified user as the owner.
     *
     * @param lobbyName The name of the lobby to be created.
     * @param targetNumberUsers Number of players the admin wants to start a game with.
     * @param user The client handler representing the owner of the lobby.
     * @return The LobbyController object associated with the newly created lobby.
     */
    public LobbyController createNewLobby(String lobbyName, int targetNumberUsers, ServerUser user, ClientHandler ch) throws LobbyNameAlreadyTakenException, InvalidLobbySettingsException {
        return model.createNewLobby(lobbyName, targetNumberUsers, user, ch);
    }

    /**
     * Allows a user to join an existing lobby with the given lobbyName.<br>
     * Adds the user to the lobby and returns the controller for that lobby.
     *
     * @param lobbyName The lobbyName of the lobby to join.
     * @param serverUser The client handler representing the user who wants to join the lobby.
     * @return The LobbyController object associated with the joined lobby.
     */
    public LobbyController joinLobby(String lobbyName, ServerUser serverUser, ClientHandler connection) throws LobbyNotFoundException, LobbyClosedException, LobbyUserAlreadyConnectedException {
        return model.joinLobby(lobbyName, serverUser, connection);
    }





    //OBSERVER METHODS
    public void addLobbyPreviewObserver(ClientHandler ch){
        model.addLobbyPreviewObserver(ch);
    }

    public void removeLobbyPreviewObserver(ClientHandler ch){
        model.removeLobbyPreviewObserver(ch);
    }
}
