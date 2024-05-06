package Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions;

/**
 * Thrown when an anonymous client attempts to perform an action that requires a logged in client
 */
public class LogInRequiredException extends MissingRequirementException {
    public LogInRequiredException(String message) {
        super(message);
    }
}
