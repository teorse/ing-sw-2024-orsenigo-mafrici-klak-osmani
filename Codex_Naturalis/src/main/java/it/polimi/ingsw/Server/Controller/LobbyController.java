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
    /**
     * Allows a server user to join the lobby using the provided client handler.
     *
     * @param serverUser The server user attempting to join the lobby.
     * @param ch         The client handler associated with the server user.
     * @throws LobbyClosedException               If the lobby is closed and cannot accept new users.
     * @throws LobbyUserAlreadyConnectedException If the user is already connected to the lobby.
     */
    public void joinLobby(ServerUser serverUser, ClientHandler ch) throws LobbyClosedException, LobbyUserAlreadyConnectedException {
        model.joinLobby(serverUser, ch);
    }





    //QUITTING METHODS
    /**
     * Handles the disconnection procedure of a user from the lobby.
     *
     * @param username The username of the user to disconnect.
     */
    public void userDisconnectionProcedure(String username){
        model.userDisconnectionProcedure(username);
    }

    /**
     * Quits the lobby for the specified user.
     *
     * @param username The username of the user quitting the lobby.
     */
    public void quitLobby(String username){
        model.quit(username);
    }





    //GETTERS
    /**
     * Retrieves the game controller associated with the lobby.
     *
     * @return The GameController object associated with the lobby.
     */
    public GameController getGameController(){
        return model.getGameController();
    }





    /**
     * Starts the game associated with the lobby.
     *
     * @param username The username of the user requesting to start the game.
     * @throws AdminRoleRequiredException           If the user does not have admin privileges to start the game.
     * @throws InvalidLobbySizeToStartGameException If the lobby size is insufficient to start the game.
     */
    public void startGame(String username) throws AdminRoleRequiredException, InvalidLobbySizeToStartGameException {
        model.startGameManually(username);
    }

    /**
     * Changes the color of a user in the lobby.
     *
     * @param username  The username of the user whose color is being changed.
     * @param newColor  The new color to assign to the user.
     * @throws UnavailableLobbyUserColorException If the requested color is unavailable or already taken.
     */
    public void changeColor(String username, LobbyUserColors newColor) throws UnavailableLobbyUserColorException {
        model.changeColor(username, newColor);
    }

    /**
     * Sends a chat message to recipients within the lobby.
     *
     * @param chatMessage The chat message record containing message details.
     * @throws NoSuchRecipientException   If the recipient specified in the message does not exist.
     * @throws InvalidRecipientException  If the recipient specified is invalid for the message.
     */
    public void sendChatMessage(ChatMessageRecord chatMessage) throws NoSuchRecipientException, InvalidRecipientException {
        model.sendChatMessage(chatMessage);
    }





    //OBSERVER METHODS
    /**
     * Adds a game controller observer for receiving updates related to game state changes.
     *
     * @param username The username of the observer.
     * @param observer The game controller observer to add.
     */
    public void addGameControllerObserver(String username, GameControllerObserver observer){
        model.addGameControllerObserver(username, observer);
    }

    /**
     * Retrieves the name of the lobby.
     *
     * @return The name of the lobby.
     */
    public String getLobbyName() {
        return model.getLobbyName();
    }
}
