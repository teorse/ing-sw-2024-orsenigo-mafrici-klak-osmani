package Exceptions.Server.LobbyExceptions;

import Exceptions.Server.ServerException;

/**
 * Base exception class for all Lobby-related exceptions.
 */
public class LobbyException extends ServerException {
    public LobbyException(String message) {
        super(message);
    }
}
