package Exceptions.Server.PermissionExceptions;

import Exceptions.Server.ServerException;

public class RoleRequiredException extends ServerException {
    public RoleRequiredException(String message) {
        super(message);
    }
}
