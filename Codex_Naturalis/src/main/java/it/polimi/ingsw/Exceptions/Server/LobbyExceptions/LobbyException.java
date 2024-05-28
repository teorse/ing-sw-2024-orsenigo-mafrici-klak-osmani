package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

import it.polimi.ingsw.Exceptions.Server.ServerException;

/**
 * Base exception class for all Lobby-related exceptions.
 */
public class LobbyException extends ServerException {
    public LobbyException(String message) {
        super(message);
    }
}
