package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

/**
 * Exception thrown when attempting to start a game with an invalid number of players in the lobby.
 */
public class InvalidLobbySizeToStartGameException extends LobbyException{

    /**
     * Constructs a new InvalidLobbySizeToStartGameException with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidLobbySizeToStartGameException(String message) {
        super(message);
    }
}
