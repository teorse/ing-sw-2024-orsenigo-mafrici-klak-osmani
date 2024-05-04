package Model.Game.States;

import Model.Game.CardPoolTypes;
import Model.Player.Player;
import Model.Player.PlayerColors;
import Server.Interfaces.LayerUser;
import Server.Model.Lobby.LobbyUser;


/**
 * Represents a state in the game where specific actions can be performed.<br>
 * This interface defines methods for the various actions that can be taken during a game.
 */
public interface GameState {
    /**
     * Places a card on the player's cardMap at the specified coordinate and updates the game's points counter.
     *
     * @param player          The player who is placing the card.
     * @param cardIndex       The index of the card in the player's hand.
     * @param coordinateIndex The index of the available coordinate in the cardMap where the card will be placed.
     * @param faceUp          True if the card should be placed face up, false if face down.
     */
    void placeCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp);


    /**
     * Draws a card from the specified card pool into the player's hand.
     *
     * @param player       The player who is drawing the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     */
    void drawCard(Player player, CardPoolTypes cardPoolType, int index);


    /**
     * Allows the player to pick a color for themselves.
     *
     * @param player The player who is picking the color.
     * @param color  The color chosen by the player.
     */
    void pickPlayerColor(Player player, PlayerColors color);


    /**
     * Allows the player to pick which objective they want to keep.
     *
     * @param player        The player who is picking the objective.
     * @param objectiveIndex The index of the objective the player is picking.
     */
    void pickPlayerObjective(Player player, int objectiveIndex);

    /**
     * Determines if the player should be removed from the game upon disconnection.
     *
     * @return True if the player should be removed on disconnection, false otherwise.
     */
    boolean shouldRemovePlayerOnDisconnect();

    /**
     * Removes a player from the game.
     *
     * @param lobbyUser The lobby user representing the player to be removed.
     */
    void removePlayer(LobbyUser lobbyUser);

    /**
     * Handles disconnection of a user from the game.<br>
     * This method prepares the game for later player removal (which will be triggered by the outside lobby by calling
     * the remove method) or helps the game to not get stuck on a disconnected player by advancing the turn to the next
     * player (if we are in one of the states that returns false for shouldRemovePlayerOnDisconnect).
     *
     * @param user The user who disconnected.
     */
    void userDisconnectionProcedure(LayerUser user);

    /**
     * Handles the quitting of a user from the game.
     *
     * @param user The user who is quitting.
     */
    void quit(LayerUser user);
}
