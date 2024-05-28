package it.polimi.ingsw.Exceptions.Server.LogInExceptions;

import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;

/**
 * Thrown when a user tries to sign up with a username that is already taken.
 */
public class AccountAlreadyExistsException extends LogInException {
    private ClientHandler source;
    private String username;

    public AccountAlreadyExistsException(ClientHandler source, String username, String message) {
        super(message);
        this.source = source;
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserAlreadyExistsException{" +
                "sign up attempt by :" + source +
                ", username :'" + username + '\'' +
                '}';
    }
}
