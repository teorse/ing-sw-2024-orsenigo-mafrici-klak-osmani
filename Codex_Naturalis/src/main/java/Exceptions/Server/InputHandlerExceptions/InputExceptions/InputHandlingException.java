package Exceptions.Server.InputHandlerExceptions.InputExceptions;

import Exceptions.Server.ServerException;

/**
 * Base exception class for input handling related exceptions.
 */
public class InputHandlingException extends ServerException {
    public InputHandlingException(String message) {
        super(message);
    }
}
