package Model.Game.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Exception thrown when a player attempts to draw an extra resource card beyond the maximum allowed during the cards setup state.
 */
public class MaxResourceCardsDrawnException extends ServerException {
    public MaxResourceCardsDrawnException(String message) {
        super(message);
    }
}
