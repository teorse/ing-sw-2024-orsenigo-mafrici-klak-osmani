package Server.Exceptions;

/**
 * Thrown when a user tries to join another lobby without quitting the current lobby.
 */
public class LobbyJoinViolationException extends ServerException{
    public LobbyJoinViolationException(String message) {
        super(message);
    }
}
