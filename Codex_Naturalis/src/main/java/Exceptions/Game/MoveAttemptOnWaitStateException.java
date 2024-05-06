package Exceptions.Game;

/**
 * Exception thrown when a player attempts a move while in a "wait" state and don't have the permission
 * to perform this action (usually thrown in asynchronous contexts once player has completed all allowed steps).
 */
public class MoveAttemptOnWaitStateException extends GameException {
    public MoveAttemptOnWaitStateException(String message) {
        super(message);
    }
}
