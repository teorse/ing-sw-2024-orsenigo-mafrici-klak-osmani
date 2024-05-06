package Client.Model.Records;

import java.io.Serializable;

/**
 * This class represents a preview of a lobby, providing an overview of its statistics without querying the lobby directly.
 *
 * @param lobbyName ATTRIBUTES
 */
public record LobbyPreviewRecord(String lobbyName, int currentUsers, int maxUsers, boolean gameStarted)implements Serializable {

    /**
     * Constructs a new LobbyPreview object with the given lobby name.
     *
     * @param lobbyName The name of the lobby.
     */
    public LobbyPreviewRecord {
    }
}
