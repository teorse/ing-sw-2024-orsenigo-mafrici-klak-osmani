package Exceptions.Server.InputHandlerExceptions.InputExceptions;

/**
 * Exception thrown when one or more expected parameters are missing from the input.
 */
public class MissingParameterException extends InputHandlingException {
    public MissingParameterException(String message) {
        super(message);
    }
}
