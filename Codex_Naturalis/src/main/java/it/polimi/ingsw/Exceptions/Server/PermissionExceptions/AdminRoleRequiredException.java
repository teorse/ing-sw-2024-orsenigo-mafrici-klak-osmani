package it.polimi.ingsw.Exceptions.Server.PermissionExceptions;

public class AdminRoleRequiredException extends RoleRequiredException {
    public AdminRoleRequiredException(String message) {
        super(message);
    }
}
