package Server.Model.Lobby.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Thrown when a user tries to join a lobby that has already started the game.
 */
public class GameAlreadyStartedException extends ServerException {
    public GameAlreadyStartedException(String message) {
        super(message);
    }
}
