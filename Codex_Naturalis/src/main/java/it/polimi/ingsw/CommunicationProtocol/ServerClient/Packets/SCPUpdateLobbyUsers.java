package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

/**
 * Packet sent from the server to the client to update the list of users in a lobby.
 * Contains a list of {@code LobbyUserRecord} objects, each representing a user in the lobby.
 * Upon receiving this packet, the client invokes {@code updateLobbyUsers} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateLobbyUsers implements ServerClientPacket{
    private final List<LobbyUserRecord> lobbyUsers;

    /**
     * Constructs a {@code SCPUpdateLobbyUsers} instance with the specified list of lobby users.
     *
     * @param lobbyUsers The list of lobby users to be updated on the client side.
     */
    public SCPUpdateLobbyUsers(List<LobbyUserRecord> lobbyUsers){
        this.lobbyUsers = lobbyUsers;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateLobbyUsers} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the list of lobby users with the information received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateLobbyUsers(lobbyUsers);
    }
}
