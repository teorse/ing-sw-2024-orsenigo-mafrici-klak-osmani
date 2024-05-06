package Exceptions.Server;

/**
 * Thrown when a user tries to join a lobby with an invalid lobby name
 */
public class LobbyNotFoundException extends ServerException{
    public LobbyNotFoundException(String message) {
        super(message);
    }
}
