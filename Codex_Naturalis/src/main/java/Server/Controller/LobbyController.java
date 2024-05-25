package Server.Controller;

import Exceptions.Server.LobbyExceptions.InvalidLobbySizeToStartGameException;
import Exceptions.Server.LobbyExceptions.UnavailableLobbyUserColorException;
import Exceptions.Server.PermissionExceptions.AdminRoleRequiredException;
import Server.Controller.InputHandler.GameControllerObserver;
import Exceptions.Server.LobbyExceptions.LobbyClosedException;
import Exceptions.Server.LobbyExceptions.LobbyUserAlreadyConnectedException;
import Server.Model.Lobby.Lobby;
import Server.Model.Lobby.LobbyUserColors;
import Server.Model.ServerUser;
import Server.Network.ClientHandler.ClientHandler;

/**
 * The LobbyController class serves as the controller for the lobby.
 * It handles operations related to managing users in the lobby and executing lobby-specific commands.
 */
public class LobbyController {
    //ATTRIBUTES
    private final Lobby model;





    //CONSTRUCTOR
    /**
     * Constructs a LobbyController object with the specified lobby model.
     *
     * @param model The Lobby object associated with this controller.
     */
    public LobbyController(Lobby model){
        this.model = model;
    }





    //JOINING METHODS
    public void joinLobby(ServerUser serverUser, ClientHandler ch) throws LobbyClosedException, LobbyUserAlreadyConnectedException {
        model.joinLobby(serverUser, ch);
    }





    //QUITTING METHODS
    public void userDisconnectionProcedure(String username){
        model.userDisconnectionProcedure(username);
    }

    public void quitLobby(String username){
        model.quit(username);
    }





    //GETTERS
    public GameController getGameController(){
        return model.getGameController();
    }





    /**
     * Starts the game associated with the lobby.
     */
    public void startGame(String username) throws AdminRoleRequiredException, InvalidLobbySizeToStartGameException {
        model.startGameManually(username);
    }

    public void changeColor(String username, LobbyUserColors newColor) throws UnavailableLobbyUserColorException{
        model.changeColor(username, newColor);
    }





    //OBSERVER METHODS
    public void addGameControllerObserver(String username, GameControllerObserver observer){
        model.addGameControllerObserver(username, observer);
    }

    public String getLobbyName() {
        return model.getLobbyName();
    }
}
