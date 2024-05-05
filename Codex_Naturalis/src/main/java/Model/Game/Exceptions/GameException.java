package Model.Game.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Represents a base exception class for exceptions occurring in the context of the game.
 * This class extends the ServerException class and provides a common base for all game-related exceptions.
 */
public class GameException extends ServerException {
    public GameException(String message) {
        super(message);
    }
}
