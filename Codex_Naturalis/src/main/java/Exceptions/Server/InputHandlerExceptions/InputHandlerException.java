package Exceptions.Server.InputHandlerExceptions;

import Exceptions.Server.ServerException;

/**
 * Base exception class for InputHandler-related exceptions
 */
public class InputHandlerException extends ServerException {
    public InputHandlerException(String message) {
        super(message);
    }
}
