package Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions;

import Exceptions.Server.InputHandlerExceptions.InputHandlerException;

/**
 * Base exception class for when a client tries to access multiple instances of the same server layer at the same time.
 */
public class MultipleAccessException extends InputHandlerException {
    public MultipleAccessException(String message) {
        super(message);
    }
}
