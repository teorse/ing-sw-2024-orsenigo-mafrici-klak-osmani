package it.polimi.ingsw.Exceptions.Server.PermissionExceptions;

import it.polimi.ingsw.Exceptions.Server.ServerException;

public class RoleRequiredException extends ServerException {
    public RoleRequiredException(String message) {
        super(message);
    }
}
