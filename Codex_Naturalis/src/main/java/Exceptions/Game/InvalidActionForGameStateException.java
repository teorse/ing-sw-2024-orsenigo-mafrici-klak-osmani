package Exceptions.Game;

/**
 * Exception thrown when a player attempts an action that is not valid in the current game state.
 */
public class InvalidActionForGameStateException extends GameException {
    public InvalidActionForGameStateException(String message) {
        super(message);
    }
}
