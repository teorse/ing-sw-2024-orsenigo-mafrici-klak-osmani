package Exceptions.Server.LobbyExceptions;

import Exceptions.Server.ServerException;

/**
 * Thrown when a user tries to join a lobby that is closed.
 */
public class LobbyClosedException extends LobbyException {
    public LobbyClosedException(String message) {
        super(message);
    }
}
