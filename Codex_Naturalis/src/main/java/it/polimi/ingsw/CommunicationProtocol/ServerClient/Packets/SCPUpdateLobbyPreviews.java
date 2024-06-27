package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

/**
 * Packet sent from the server to the client to update the list of lobby previews.
 * Contains a list of {@code LobbyPreviewRecord} objects, each representing a preview of a lobby.
 * Upon receiving this packet, the client invokes {@code updateLobbyPreviews} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateLobbyPreviews implements ServerClientPacket{
    private final List<LobbyPreviewRecord> lobbyPreviews;

    /**
     * Constructs a {@code SCPUpdateLobbyPreviews} instance with the specified list of lobby previews.
     *
     * @param lobbyPreviews The list of lobby previews to be updated on the client side.
     */
    public SCPUpdateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviews){
        this.lobbyPreviews = lobbyPreviews;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateLobbyPreviews} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the list of lobby previews with the information received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateLobbyPreviews(lobbyPreviews);
    }
}
