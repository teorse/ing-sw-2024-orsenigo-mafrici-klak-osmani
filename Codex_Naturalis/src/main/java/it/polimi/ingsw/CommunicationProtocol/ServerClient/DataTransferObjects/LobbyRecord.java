package it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects;

import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a record containing information about a lobby in a game.
 *
 * <p>A {@code LobbyRecord} contains details such as the name of the lobby, the target number of users, and a list of
 * available user colors in the lobby.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record LobbyRecord(String lobbyName, int targetNumberUsers, List<LobbyUserColors> availableUserColors, boolean gameStartable)
        implements Serializable {

    /**
     * Constructs a new {@code LobbyRecord} with the specified parameters.
     *
     * @param lobbyName the name of the lobby
     * @param targetNumberUsers the target number of users in the lobby
     * @param availableUserColors the list of available user colors in the lobby
     */
    public LobbyRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}
