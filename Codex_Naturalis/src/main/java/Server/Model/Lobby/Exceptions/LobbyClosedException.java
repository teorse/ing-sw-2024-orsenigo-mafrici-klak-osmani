package Server.Model.Lobby.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Thrown when a user tries to join a lobby that is closed.
 */
public class LobbyClosedException extends ServerException {
    public LobbyClosedException(String message) {
        super(message);
    }
}
