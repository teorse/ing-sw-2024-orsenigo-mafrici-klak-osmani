package Client.Model.Records;

import Server.Model.Lobby.LobbyUserColors;

import java.util.List;

public record LobbyRecord(String lobbyName, int targetNumberUsers, List<LobbyUserColors> availableUserColors) {
    /**
     * Javadoc placeholder
     *
     * @param lobbyName
     * @param targetNumberUsers
     * @param availableUserColors
     */
    public LobbyRecord {}
}
