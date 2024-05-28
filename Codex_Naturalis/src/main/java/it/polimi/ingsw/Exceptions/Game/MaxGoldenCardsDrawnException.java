package it.polimi.ingsw.Exceptions.Game;

/**
 * Exception thrown when a player attempts to draw an extra golden card beyond the maximum allowed during the cards setup state.
 */
public class MaxGoldenCardsDrawnException extends GameException {
    public MaxGoldenCardsDrawnException(String message) {
        super(message);
    }
}
