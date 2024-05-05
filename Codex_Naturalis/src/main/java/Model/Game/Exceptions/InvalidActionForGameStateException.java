package Model.Game.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Exception thrown when a player attempts an action that is not valid in the current game state.
 */
public class InvalidActionForGameStateException extends ServerException {
    public InvalidActionForGameStateException(String message) {
        super(message);
    }
}
