package Client.Model.Records;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a record containing a preview of a lobby in a game.
 *
 * <p>A {@code LobbyPreviewRecord} provides basic information about a lobby, such as its name, the number of
 * current users, the maximum number of users allowed, and whether a game has started in the lobby.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record LobbyPreviewRecord(String lobbyName, int currentUsers, int maxUsers, boolean gameStarted)
        implements Serializable {

    /**
     * Constructs a new {@code LobbyPreviewRecord} with the specified parameters.
     *
     * @param lobbyName the name of the lobby
     * @param currentUsers the number of current users in the lobby
     * @param maxUsers the maximum number of users allowed in the lobby
     * @param gameStarted indicates whether a game has started in the lobby
     */
    public LobbyPreviewRecord {
        // No additional implementation needed as records automatically generate a constructor
    }





    //EQUALS AND HASH

    /**
     *Two Lobby Previews are equal if they have the same lobby name.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LobbyPreviewRecord that = (LobbyPreviewRecord) o;
        return Objects.equals(lobbyName, that.lobbyName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lobbyName);
    }
}
