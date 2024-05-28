package it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.InputExceptions;

import it.polimi.ingsw.Exceptions.Server.ServerException;

/**
 * Base exception class for input handling related exceptions.
 */
public class InputHandlingException extends ServerException {
    public InputHandlingException(String message) {
        super(message);
    }
}
