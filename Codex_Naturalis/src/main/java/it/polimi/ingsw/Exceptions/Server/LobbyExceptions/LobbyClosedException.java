package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

/**
 * Thrown when a user tries to join a lobby that is closed.
 */
public class LobbyClosedException extends LobbyException {
    public LobbyClosedException(String message) {
        super(message);
    }
}
