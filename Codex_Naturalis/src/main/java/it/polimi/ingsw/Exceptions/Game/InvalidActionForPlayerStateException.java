package it.polimi.ingsw.Exceptions.Game;

import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

/**
 * Exception thrown when a player attempts an action that is not valid in their current state.<br>
 * See {@link PlayerStates} for valid player states and corresponding actions.
 */
public class InvalidActionForPlayerStateException extends GameException {
    public InvalidActionForPlayerStateException(String message) {
        super(message);
    }
}
