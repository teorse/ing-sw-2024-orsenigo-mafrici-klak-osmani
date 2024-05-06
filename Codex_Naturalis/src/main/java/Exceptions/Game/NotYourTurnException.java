package Exceptions.Game;

/**
 * Exception thrown when a player attempts to perform a move when it is not their turn.
 */
public class NotYourTurnException extends GameException {

    public NotYourTurnException(String message) {
        super(message);
    }
}
