package Server.Model.Game.Game.States;

import Client.Model.Records.GameRecord;
import Exceptions.Game.*;
import Server.Model.Game.Game.CardPoolTypes;
import Server.Model.Game.Game.Game;
import Server.Model.Game.Player.Player;
import Server.Model.Lobby.ObserverRelay;

import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a state in the game where specific actions can be performed.<br>
 * This abstract class defines methods for the various actions that can be taken during a game
 * and provides a default implementation for some of them.
 */
public abstract class GameState {
    //ATTRIBUTES
    protected final Game game;
    protected final List<Player> players;
    protected final ObserverRelay gameObserverRelay;
    protected Logger logger;





    //CONSTRUCTOR
    /**
     * Constructs a new GameState.
     *
     * @param game The game instance associated with this state.
     */
    protected GameState(Game game) {
        logger = Logger.getLogger(GameState.class.getName());

        logger.info("Initializing GameState");

        this.game = game;
        players = game.getPlayers();
        gameObserverRelay = game.getGameObserverRelay();

        logger.info("GameState Initialized");
    }

    /**
     * Attempts to play a card. The default implementation throws an exception as this action is not allowed in the current state.
     * Subclasses can override this method to define the behavior for playing a card in the specific state.
     *
     * @param player          The player attempting to place the card.
     * @param cardIndex       The index of the card in the player's hand.
     * @param coordinateIndex The index of the available coordinate in the cardMap where the card will be placed.
     * @param faceUp          True if the card should be placed face up, false if face down.
     * @throws MoveAttemptOnWaitStateException      If the move is attempted in a wait state.
     * @throws NotYourTurnException                 If it is not the player's turn.
     * @throws InvalidActionForPlayerStateException If the action is invalid for the player's state.
     * @throws InvalidActionForGameStateException   If the action is not allowed in the current game state.
     */
    public void playCard(Player player, int cardIndex, int coordinateIndex, boolean faceUp) throws MoveAttemptOnWaitStateException, NotYourTurnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException{
        throw new InvalidActionForGameStateException("You can't place cards in this state.");
    }

    /**
     * Attempts to draw a card. The default implementation throws an exception as this action is not allowed in the current state.
     * Subclasses can override this method to define the behavior for drawing a card in the specific state.
     *
     * @param player       The player attempting to draw the card.
     * @param cardPoolType The type of card pool from which the card will be drawn.
     * @param index        The index of the card in the card pool.
     * @throws MoveAttemptOnWaitStateException      If the move is attempted in a wait state.
     * @throws NotYourTurnException                 If it is not the player's turn.
     * @throws InvalidActionForPlayerStateException If the action is invalid for the player's state.
     * @throws InvalidActionForGameStateException   If the action is not allowed in the current game state.
     * @throws MaxGoldenCardsDrawnException         If the player has drawn the maximum number of golden cards.
     * @throws MaxResourceCardsDrawnException       If the player has drawn the maximum number of resource cards.
     */
    public void drawCard(Player player, CardPoolTypes cardPoolType, int index) throws MoveAttemptOnWaitStateException, NotYourTurnException, InvalidActionForPlayerStateException, InvalidActionForGameStateException, MaxGoldenCardsDrawnException, MaxResourceCardsDrawnException {
        throw new InvalidActionForGameStateException("You can't draw cards in this state.");
    }

    /**
     * Attempts to pick a player objective. The default implementation throws an exception as this action is not allowed in the current state.
     * Subclasses can override this method to define the behavior for picking a player objective in the specific state.
     *
     * @param player          The player attempting to pick the objective.
     * @param objectiveIndex  The index of the objective the player is picking.
     * @throws MoveAttemptOnWaitStateException      If the move is attempted in a wait state.
     * @throws InvalidActionForPlayerStateException If the action is invalid for the player's state.
     * @throws InvalidActionForGameStateException   If the action is not allowed in the current game state.
     */
    public void pickPlayerObjective(Player player, int objectiveIndex) throws MoveAttemptOnWaitStateException, InvalidActionForPlayerStateException, InvalidActionForGameStateException {
        throw new InvalidActionForGameStateException("You can't pick your secret objective in this game state");
    }

    /**
     * Determines if the player should be removed from the game upon disconnection. The default implementation returns true,
     * Subclasses can override this method if they want to implement resilience to disconnections.
     *
     * @return True if the player should be removed on disconnection, false otherwise.
     */
    public boolean shouldRemovePlayerOnDisconnect() {
        return true;
    }

    /**
     * Removes a player from the game.
     *
     * @param player The player to remove.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Handles disconnection of a user from the game.<br>
     * This method prepares the game for later player removal, it does not directly remove the player
     * (the removal will be triggered by the outside lobby by calling
     * the remove method).
     *
     * @param player The user who disconnected.
     */
    public void userDisconnectionProcedure(Player player) {
        logger.info("Player "+player.getUsername()+"disconnected, waiting for signal from lobby to remove the player");
    }

    /**
     * Handles the reconnection of a player to the game.<br>
     * Classes that override this  method should send to the reconnecting player all the packets containing
     * Player specific information regarding state specific variables.
     *
     * @param player The player who reconnected.
     */
    public void userReconnectionProcedure(Player player) {
        logger.info("Player "+player.getUsername()+" reconnected to the game.");
    }

    /**
     * Handles a player's request to quit the game.
     *
     * @param player The player requesting to quit.
     */
    public void quit(Player player) {
        logger.info("Player "+player.getUsername()+" requested to quit from the game.");
        removePlayer(player);
    }

    /**
     * Converts the current game state to a GameRecord.
     *
     * @return A GameRecord representing the current game state.
     */
    public GameRecord toRecord() {
        return new GameRecord(0, false, false, false);
    }

    /**
     * Advances the game to the next state.
     * This method must be implemented by subclasses to define the transition to the next state.
     */
    protected abstract void nextState();
}
