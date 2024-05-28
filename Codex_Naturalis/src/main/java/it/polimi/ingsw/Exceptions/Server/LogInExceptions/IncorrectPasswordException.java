package it.polimi.ingsw.Exceptions.Server.LogInExceptions;

/**
 * Thrown when a user tries to log in with an incorrect password.
 */
public class IncorrectPasswordException extends LogInException {
    private String account;

    public IncorrectPasswordException(String account, String message) {
        super(message);
        this.account = account;
    }

    @Override
    public String toString() {
        return "IncorrectPasswordException{" +
                "account='" + account + '\'' +
                '}';
    }
}
