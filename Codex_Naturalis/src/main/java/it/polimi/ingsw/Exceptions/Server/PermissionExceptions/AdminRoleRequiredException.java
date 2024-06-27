package it.polimi.ingsw.Exceptions.Server.PermissionExceptions;

/**
 * Exception thrown when an operation requires the user to have an admin role but the user does not have it.
 */
public class AdminRoleRequiredException extends RoleRequiredException {

    /**
     * Constructs a new AdminRoleRequiredException with the specified detail message.
     *
     * @param message The detail message.
     */
    public AdminRoleRequiredException(String message) {
        super(message);
    }
}
