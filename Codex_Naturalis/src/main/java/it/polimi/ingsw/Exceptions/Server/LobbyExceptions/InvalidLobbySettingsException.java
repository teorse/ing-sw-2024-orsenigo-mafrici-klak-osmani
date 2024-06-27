package it.polimi.ingsw.Exceptions.Server.LobbyExceptions;

/**
 * Exception thrown when invalid settings are provided for creating or configuring a lobby.
 */
public class InvalidLobbySettingsException extends LobbyException {

    /**
     * Constructs a new InvalidLobbySettingsException with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidLobbySettingsException(String message) {
        super(message);
    }
}
