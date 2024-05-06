package Client.Model.Records;

import Server.Model.Lobby.LobbyRoles;
import Server.Model.Lobby.LobbyUserColors;
import Server.Model.Lobby.LobbyUserConnectionStates;

public record LobbyUserRecord(String username, LobbyRoles role, LobbyUserColors color,
                              LobbyUserConnectionStates connectionStatus) {
    /**
     * constructor javadoc placeholder
     *
     * @param username
     * @param role
     * @param color
     * @param connectionStatus
     */
    public LobbyUserRecord {}
}
