package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

/**
 * Exception thrown when attempting to send a chat message to a non-existing recipient.
 */
public class NoSuchRecipientException extends LobbyException{

    /**
     * Constructs a new NoSuchRecipientException with the specified detail message.
     *
     * @param message The detail message.
     */
    public NoSuchRecipientException(String message) {
        super(message);
    }
}
