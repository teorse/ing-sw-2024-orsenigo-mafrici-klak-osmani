package Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions;

/**
 * Thrown when a client that has not yet joined a game attempts to perform an action that requires a game
 */
public class GameRequiredException extends MissingRequirementException {
    public GameRequiredException(String message) {
        super(message);
    }
}
