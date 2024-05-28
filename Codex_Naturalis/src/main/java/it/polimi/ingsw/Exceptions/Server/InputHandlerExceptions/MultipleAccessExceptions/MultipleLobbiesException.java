package it.polimi.ingsw.Exceptions.Server.InputHandlerExceptions.MultipleAccessExceptions;

/**
 * Thrown when a user tries to create a new lobby or join another lobby while already being in a lobby.
 */
public class MultipleLobbiesException extends MultipleAccessException {
    public MultipleLobbiesException(String message) {
        super(message);
    }
}
