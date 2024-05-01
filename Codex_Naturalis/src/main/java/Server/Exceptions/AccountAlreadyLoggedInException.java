package Server.Exceptions;

import Server.Network.ClientHandler.ClientHandler;

/**
 * Thrown when a user tries to log into an account where someone is already logged into the account.
 */
public class AccountAlreadyLoggedInException extends ServerException{
    private final ClientHandler source;
    private final String account;

    public AccountAlreadyLoggedInException(ClientHandler source, String account, String message) {
        super(message);
        this.source = source;
        this.account = account;
    }

    @Override
    public String toString() {
        return "SingleLoginViolationException{" +
                "log in attempt by :" + source +
                ", account:'" + account + '\'' +
                '}';
    }
}
