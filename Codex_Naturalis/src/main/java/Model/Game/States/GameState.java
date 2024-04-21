package Model.Game.States;

import Model.Game.CardPoolTypes;
import Model.Player.Player;
import Model.Player.PlayerColors;


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
    public void placeCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp);


    /**
     * Draws a card from the specified card pool into the player's hand.
     *
     * @param player       The player who is drawing the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     */
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index);


    /**
     * Allows the player to pick a color for themselves.
     *
     * @param player The player who is picking the color.
     * @param color  The color chosen by the player.
     */
    public void pickPlayerColor(Player player, PlayerColors color);


    /**
     * Allows the player to pick which objective they want to keep.
     *
     * @param player        The player who is picking the objective.
     * @param objectiveIndex The index of the objective the player is picking.
     */
    public void pickPlayerObjective(Player player, int objectiveIndex);
}
