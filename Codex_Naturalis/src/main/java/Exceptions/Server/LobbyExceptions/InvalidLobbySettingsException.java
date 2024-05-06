package Exceptions.Server.LobbyExceptions;

import Exceptions.Server.ServerException;

public class InvalidLobbySettingsException extends LobbyException {
    public InvalidLobbySettingsException(String message) {
        super(message);
    }
}
