package Exceptions.Server.LogInExceptions;

import Exceptions.Server.InputHandlerExceptions.InputHandlerException;

/**
 * Base exception class for server log-in related exceptions
 */
public class LogInException extends InputHandlerException {
    public LogInException(String message) {
        super(message);
    }
}
