package Server.Controller;

import Exceptions.Server.LobbyExceptions.UnavailableLobbyUserColorException;
import Server.Controller.InputHandler.LobbyInputHandler;
import Server.Interfaces.LayerUser;
import Exceptions.Server.LobbyExceptions.LobbyClosedException;
import Exceptions.Server.LobbyExceptions.LobbyUserAlreadyConnectedException;
import Server.Model.Lobby.Lobby;
import Server.Model.Lobby.LobbyUser;
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
    public void userDisconnectionProcedure(LayerUser user){
        model.userDisconnectionProcedure(user);
    }

    public void quitLobby(LayerUser user){
        model.quit(user);
    }





    //GETTERS
    public String getLobbyName(){
        return model.getLobbyName();
    }

    public GameController getGameController(){
        return model.getGameController();
    }

    public LobbyUser getLobbyUserByServerUser(ServerUser serverUser){
        return model.getLobbyUserByServerUser(serverUser);
    }





    //DEMO METHODS
    /**
     * Executes Command1 demo in the lobby.
     */
    public void Command1(){
        model.Command1();
    }

    /**
     * Executes Command2 demo in the lobby.
     */
    public void Command2(){
        model.Command2();
    }

    /**
     * Executes Command3 demo in the lobby.
     */
    public void Command3(){
        model.Command3();
    }

    /**
     * Starts the game associated with the lobby.
     */
    public void startGame(){
        model.startGame();
    }

    public void changeColor(LobbyUser user, LobbyUserColors newColor) throws UnavailableLobbyUserColorException{
        model.changeColor(user, newColor);
    }





    //OBSERVER METHODS
    public void addGameControllerObserver(LobbyInputHandler observer){
        model.addGameControllerObserver(observer);
    }

    public void removeGameControllerObserver(LobbyInputHandler observer){
        model.removeGameControllerObserver(observer);
    }
}
