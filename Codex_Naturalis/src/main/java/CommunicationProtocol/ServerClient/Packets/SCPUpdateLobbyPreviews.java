package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

public class SCPUpdateLobbyPreviews implements ServerClientPacket{
    private final List<LobbyPreviewRecord> lobbyPreviews;

    public SCPUpdateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviews){
        this.lobbyPreviews = lobbyPreviews;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateLobbyPreviews(lobbyPreviews);
    }
}
