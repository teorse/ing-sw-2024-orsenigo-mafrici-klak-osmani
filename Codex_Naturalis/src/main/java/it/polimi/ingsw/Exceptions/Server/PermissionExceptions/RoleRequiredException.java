package it.polimi.ingsw.Exceptions.Server.PermissionExceptions;

import it.polimi.ingsw.Exceptions.Server.ServerException;

/**
 * Exception thrown when a specific role is required for performing an action,
 * but the current user does not have the necessary role.
 * This exception typically indicates a permission-related error.
 */
public class RoleRequiredException extends ServerException {

    /**
     * Constructs a new RoleRequiredException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public RoleRequiredException(String message) {
        super(message);
    }
}
