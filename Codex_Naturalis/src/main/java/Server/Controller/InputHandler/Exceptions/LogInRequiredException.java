package Server.Controller.InputHandler.Exceptions;

import Server.Exceptions.ServerException;

public class LogInRequiredException extends ServerException {
    public LogInRequiredException(String message) {
        super(message);
    }
}
