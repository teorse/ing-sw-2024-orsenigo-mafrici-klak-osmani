package Client.Model.Records;

import Server.Model.Lobby.LobbyUserColors;

import java.io.Serializable;
import java.util.List;

public record LobbyRecord(String lobbyName, int targetNumberUsers, List<LobbyUserColors> availableUserColors) implements Serializable {
    /**
     * Javadoc placeholder
     *
     * @param lobbyName
     * @param targetNumberUsers
     * @param availableUserColors
     */
    public LobbyRecord {}
}
