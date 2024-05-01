package Server.Model.Lobby.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Thrown when a user tries to join a lobby that has reached the maximum number of participants.
 */
public class LobbyIsAlreadyFullException extends ServerException {
    public LobbyIsAlreadyFullException(String message) {
        super(message);
    }
}
