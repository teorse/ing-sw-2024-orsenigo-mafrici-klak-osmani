package Server.Controller.InputHandler.Exceptions;

import Server.Exceptions.ServerException;

public class NotInGameException extends ServerException {
    public NotInGameException(String message) {
        super(message);
    }
}
