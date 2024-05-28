package it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions;

import it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.InputHandlerException;

/**
 * Base exception class for exceptions that arise when trying to perform an action for which a requirement is missing
 */
public class MissingRequirementException extends InputHandlerException {
    public MissingRequirementException(String message) {
        super(message);
    }
}
