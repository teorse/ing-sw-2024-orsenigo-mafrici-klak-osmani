package Server.Model.Lobby.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Thrown when a user tries to reconnect to the lobby with an account that is not registered as DISCONNECTED.
 */
public class LobbyUserAlreadyConnectedException extends ServerException {
    public LobbyUserAlreadyConnectedException(String message) {
        super(message);
    }
}
