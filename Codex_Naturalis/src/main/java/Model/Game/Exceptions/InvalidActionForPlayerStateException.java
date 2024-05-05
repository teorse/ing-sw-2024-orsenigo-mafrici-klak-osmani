package Model.Game.Exceptions;

import Server.Exceptions.ServerException;

/**
 * Exception thrown when a player attempts an action that is not valid in their current state.<br>
 * See {@link Model.Player.PlayerStates} for valid player states and corresponding actions.
 */
public class InvalidActionForPlayerStateException extends GameException {
    public InvalidActionForPlayerStateException(String message) {
        super(message);
    }
}
