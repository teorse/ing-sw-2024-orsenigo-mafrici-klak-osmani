package Model.Game.States;

import Exceptions.Game.*;
import Model.Game.CardPoolTypes;
import Model.Player.Player;
import Server.Model.Lobby.LobbyUserColors;
import Server.Interfaces.LayerUser;
import Server.Model.Lobby.LobbyUser;


/**
 * Represents a state in the game where specific actions can be performed.<br>
 * This interface defines methods for the various actions that can be taken during a game.
 */
public interface GameState {
    /**
     * Places a card on the player's cardMap at the specified coordinate and updates the player's points counter.
     *
     * @param player          The player who is placing the card.
     * @param cardIndex       The index of the card in the player's hand.
     * @param coordinateIndex The index of the available coordinate in the cardMap where the card will be placed.
     * @param faceUp          True if the card should be placed face up, false if face down.
     * @throws MoveAttemptOnWaitStateException          If a player attempts a move while in a "wait" state.
     * @throws NotYourTurnException                     If a player attempts to make a move when it is not their turn.
     * @throws InvalidActionForPlayerStateException     If a player attempts an action that is not valid in their current state.
     * @throws InvalidActionForGameStateException       If a player attempts an action that is not valid in the current game state.
     */
    void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws MoveAttemptOnWaitStateException, NotYourTurnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException;


    /**
     * Draws a card from the specified card pool into the player's hand.
     *
     * @param player       The player who is drawing the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     * @throws MoveAttemptOnWaitStateException          If a player attempts a move while in a "wait" state.
     * @throws NotYourTurnException                     If a player attempts to make a move when it is not their turn.
     * @throws InvalidActionForPlayerStateException     If a player attempts an action that is not valid in their current state.
     * @throws InvalidActionForGameStateException       If a player attempts an action that is not valid in the current game state.
     * @throws MaxGoldenCardsDrawnException             If a player attempts to draw an extra golden card beyond the maximum allowed during the cards setup state.
     * @throws MaxResourceCardsDrawnException           If a player attempts to draw an extra resource card beyond the maximum allowed during the cards setup state.
     */
    void drawCard(Player player, CardPoolTypes cardPoolType, int index)throws MoveAttemptOnWaitStateException, NotYourTurnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, MaxGoldenCardsDrawnException, MaxResourceCardsDrawnException;

    /**
     * Allows the player to pick which objective they want to keep.
     *
     * @param player        The player who is picking the objective.
     * @param objectiveIndex The index of the objective the player is picking.
     * @throws MoveAttemptOnWaitStateException      If a player attempts a move while in a "wait" state.
     * @throws InvalidActionForPlayerStateException If a player attempts an action that is not valid in their current state.
     * @throws InvalidActionForGameStateException   If a player attempts an action that is not valid in the current game state.
     */
    void pickPlayerObjective(Player player, int objectiveIndex)throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException;

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
