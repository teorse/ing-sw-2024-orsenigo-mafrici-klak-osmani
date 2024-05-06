package Exceptions.Server.InputHandlerExceptions.InputExceptions;

/**
 * Exception thrown when an input value does not match the expected type.
 */
public class MalformedInputException extends InputHandlingException {
    public MalformedInputException(String message) {
        super(message);
    }
}
