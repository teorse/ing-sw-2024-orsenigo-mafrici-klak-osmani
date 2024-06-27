package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

/**
 * Exception thrown when attempting to send a chat message to an invalid recipient.
 */
public class InvalidRecipientException extends LobbyException{

    /**
     * Constructs a new InvalidRecipientException with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidRecipientException(String message) {
        super(message);
    }
}
