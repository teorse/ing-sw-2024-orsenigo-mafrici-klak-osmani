package it.polimi.ingsw.Exceptions.Game;

/**
 * Exception thrown when a player attempts to draw an extra resource card beyond the maximum allowed during the cards setup state.
 */
public class MaxResourceCardsDrawnException extends GameException {
    public MaxResourceCardsDrawnException(String message) {
        super(message);
    }
}
