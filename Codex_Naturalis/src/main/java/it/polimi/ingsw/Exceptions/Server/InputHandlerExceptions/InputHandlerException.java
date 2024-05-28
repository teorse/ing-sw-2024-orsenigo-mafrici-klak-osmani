package it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions;

import it.polimi.ingsw.Exceptions.Server.ServerException;

/**
 * Base exception class for InputHandler-related exceptions
 */
public class InputHandlerException extends ServerException {
    public InputHandlerException(String message) {
        super(message);
    }
}
