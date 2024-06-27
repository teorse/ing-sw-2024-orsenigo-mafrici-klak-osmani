package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

/**
 * Exception thrown when a user attempts to select a lobby color that is not available.
 * This typically occurs when all available colors in a lobby have already been assigned to other users.
 */
public class UnavailableLobbyUserColorException extends LobbyException{

    /**
     * Constructs a new UnavailableLobbyUserColorException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public UnavailableLobbyUserColorException(String message) {
        super(message);
    }
}
