package it.polimi.ingsw.Exceptions.Server.LogInExceptions;

import it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.InputHandlerException;

/**
 * Base exception class for server log-in related exceptions
 */
public class LogInException extends InputHandlerException {
    public LogInException(String message) {
        super(message);
    }
}
