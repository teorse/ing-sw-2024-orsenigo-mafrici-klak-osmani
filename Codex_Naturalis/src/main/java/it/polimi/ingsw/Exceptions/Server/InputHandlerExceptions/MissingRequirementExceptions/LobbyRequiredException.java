package it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MissingRequirementExceptions;

/**
 * Thrown when a client that has not yet joined a lobby attempts to perform an action that requires a lobby
 */
public class LobbyRequiredException extends MissingRequirementException {
    public LobbyRequiredException(String message) {
        super(message);
    }
}
