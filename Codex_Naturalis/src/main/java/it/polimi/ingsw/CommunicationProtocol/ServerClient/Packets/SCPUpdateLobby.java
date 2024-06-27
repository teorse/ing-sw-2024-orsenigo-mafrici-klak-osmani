package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to update lobby information.
 * Contains the {@code LobbyRecord} encapsulating details about the updated lobby state.
 * Upon receiving this packet, the client invokes {@code updateLobby} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateLobby implements ServerClientPacket{

    private final LobbyRecord lobby;

    /**
     * Constructs a {@code SCPUpdateLobby} instance with the specified lobby record.
     *
     * @param lobby The lobby record to be updated on the client side.
     */
    public SCPUpdateLobby(LobbyRecord lobby) {
        this.lobby = lobby;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateLobby} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the lobby state with the information received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateLobby(lobby);
    }
}
