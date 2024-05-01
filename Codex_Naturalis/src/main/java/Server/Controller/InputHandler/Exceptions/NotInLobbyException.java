package Server.Controller.InputHandler.Exceptions;

import Server.Exceptions.ServerException;

public class NotInLobbyException extends ServerException {
    public NotInLobbyException(String message) {
        super(message);
    }
}
