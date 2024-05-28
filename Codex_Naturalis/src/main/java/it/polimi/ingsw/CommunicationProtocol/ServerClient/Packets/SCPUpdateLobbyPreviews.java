package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
