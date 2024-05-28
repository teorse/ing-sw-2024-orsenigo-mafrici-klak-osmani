package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.Exceptions.Server.LobbyExceptions.*;
import it.polimi.ingsw.Exceptions.Server.PermissionExceptions.AdminRoleRequiredException;
import it.polimi.ingsw.Server.Controller.InputHandler.GameControllerObserver;
import it.polimi.ingsw.Server.Model.Lobby.Lobby;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;
import it.polimi.ingsw.Server.Model.Server.ServerUser;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;

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

    public void changeColor(String username, LobbyUserColors newColor) throws UnavailableLobbyUserColorException {
        model.changeColor(username, newColor);
    }

    public void sendChatMessage(ChatMessageRecord chatMessage) throws NoSuchRecipientException, InvalidRecipientException {
        model.sendChatMessage(chatMessage);
    }





    //OBSERVER METHODS
    public void addGameControllerObserver(String username, GameControllerObserver observer){
        model.addGameControllerObserver(username, observer);
    }

    public String getLobbyName() {
        return model.getLobbyName();
    }
}
