package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

/**
 * Thrown when a user tries to reconnect to the lobby with an account that is not registered as DISCONNECTED.
 */
public class LobbyUserAlreadyConnectedException extends LobbyException {
    public LobbyUserAlreadyConnectedException(String message) {
        super(message);
    }
}
