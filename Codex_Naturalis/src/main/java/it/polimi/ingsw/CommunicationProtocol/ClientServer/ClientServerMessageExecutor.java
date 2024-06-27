package it.polimi.ingsw.CommunicationProtocol.ClientServer;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

/**
 * Interface for executing client-server messages in a game application.
 */
public interface ClientServerMessageExecutor {

    //SERVER LAYER COMMANDS

    /**
     * Logs the user in with the given username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    void logIn(String username, String password);

    /**
     * Signs the user up with the given username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     */
    void signUp(String username, String password);

    /**
     * Logs the user out of the system.
     */
    void logOut();

    /**
     * Views the previews of available lobbies.
     */
    void viewLobbyPreviews();

    /**
     * Stops viewing the previews of available lobbies.
     */
    void stopViewingLobbyPreviews();

    /**
     * Starts a new lobby with the given name and size.
     *
     * @param lobbyName the name of the new lobby
     * @param lobbySize the size of the new lobby
     */
    void startLobby(String lobbyName, int lobbySize);

    /**
     * Joins the lobby with the given name.
     *
     * @param lobbyName the name of the lobby to join
     */
    void joinLobby(String lobbyName);

    //LOBBY LAYER COMMANDS

    /**
     * Starts the game in the current lobby.
     */
    void startGame();

    /**
     * Quits the current lobby.
     */
    void quitLobby();

    /**
     * Changes the user's color in the lobby.
     *
     * @param newColor the new color for the user
     */
    void changeColor(LobbyUserColors newColor);

    /**
     * Sends a chat message in the current lobby.
     *
     * @param chatMessage the chat message to send
     */
    void sendChatMessage(ChatMessageRecord chatMessage);

    //GAME LAYER COMMANDS

    /**
     * Plays a card from the user's hand.
     *
     * @param cardIndex       the index of the card in the user's hand
     * @param coordinateIndex the index of the coordinate where the card will be played
     * @param faceUp          whether the card is played face up
     */
    void playCard(int cardIndex, int coordinateIndex, boolean faceUp);

    /**
     * Draws a card from the specified card pool.
     *
     * @param cardPoolType the type of card pool to draw from
     * @param cardIndex    the index of the card in the card pool
     */
    void drawCard(CardPoolTypes cardPoolType, int cardIndex);

    /**
     * Picks an objective in the game.
     *
     * @param objectiveIndex the index of the objective to pick
     */
    void pickObjective(int objectiveIndex);
}
