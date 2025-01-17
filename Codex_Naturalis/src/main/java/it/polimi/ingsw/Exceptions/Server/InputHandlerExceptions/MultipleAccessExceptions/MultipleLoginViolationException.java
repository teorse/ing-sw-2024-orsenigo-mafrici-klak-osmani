package it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions;

import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;

/**
 * Thrown when a user tries to log in or sign up to another account without logging out of the current one.
 */
public class MultipleLoginViolationException extends MultipleAccessException {
    private final ClientHandler source;
    private final String currentUsername;
    private final String targetUsername;

    public MultipleLoginViolationException(ClientHandler source, String currentUsername, String targetUsername, String message) {
        super(message);
        this.source = source;
        this.currentUsername = currentUsername;
        this.targetUsername = targetUsername;
    }

    @Override
    public String toString() {
        return "MultipleLoginViolationException{" +
                "source=" + source +
                ", currentUsername='" + currentUsername + '\'' +
                ", targetUsername='" + targetUsername + '\'' +
                '}';
    }
}
