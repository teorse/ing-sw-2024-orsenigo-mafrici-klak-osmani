package Network.ServerClientPacket;

import Client.Model.Records.LobbyPreviewRecord;
import Network.ServerMessageExecutor;

import java.util.List;

public class SCPUpdateLobbyPreviews implements ServerClientPacket{
    private final List<LobbyPreviewRecord> lobbyPreviews;

    public SCPUpdateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviews){
        this.lobbyPreviews = lobbyPreviews;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.updateLobbyPreviews(lobbyPreviews);
    }
}
