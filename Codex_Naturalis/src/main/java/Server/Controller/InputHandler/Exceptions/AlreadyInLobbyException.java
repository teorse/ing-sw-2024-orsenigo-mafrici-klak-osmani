package Server.Controller.InputHandler.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Thrown when a user tries to create a new lobby or join another lobby while already being in a lobby.
 */
public class AlreadyInLobbyException extends ServerException {
    public AlreadyInLobbyException(String message) {
        super(message);
    }
}
