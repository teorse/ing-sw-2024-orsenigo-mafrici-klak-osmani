package CommunicationProtocol.ServerClient.DataTransferObjects;

import Server.Model.Lobby.LobbyRoles;
import Server.Model.Lobby.LobbyUserColors;
import Server.Model.Lobby.LobbyUserConnectionStates;

import java.io.Serializable;

/**
 * Represents a record containing information about a user in a lobby.
 *
 * <p>A {@code LobbyUserRecord} contains details such as the username, role, color, and connection status of the user
 * within a lobby.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record LobbyUserRecord(String username, LobbyRoles role, LobbyUserColors color,
                              LobbyUserConnectionStates connectionStatus) implements Serializable {

    /**
     * Constructs a new {@code LobbyUserRecord} with the specified parameters.
     *
     * @param username the username of the user
     * @param role the role of the user in the lobby
     * @param color the color assigned to the user in the lobby
     * @param connectionStatus the connection status of the user
     */
    public LobbyUserRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}
