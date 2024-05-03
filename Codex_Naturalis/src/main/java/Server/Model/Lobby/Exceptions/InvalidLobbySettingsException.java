package Server.Model.Lobby.Exceptions;

import Server.Exceptions.ServerException;

public class InvalidLobbySettingsException extends ServerException {
    public InvalidLobbySettingsException(String message) {
        super(message);
    }
}
