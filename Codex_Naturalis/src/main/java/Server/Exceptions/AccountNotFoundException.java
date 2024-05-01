package Server.Exceptions;

import Server.Network.ClientHandler.ClientHandler;

/**
 * Thrown when a user tries to log in with an account that does not exist.
 */
public class AccountNotFoundException extends ServerException{
    private final ClientHandler source;
    private final String username;

    public AccountNotFoundException(ClientHandler source, String username, String message) {
        super(message);
        this.source = source;
        this.username = username;
    }

    @Override
    public String toString() {
        return "AccountNotFoundException{" +
                "source=" + source +
                ", username='" + username + '\'' +
                '}';
    }
}
