package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPUpdateLobby implements ServerClientPacket{

    private final LobbyRecord lobby;

    public SCPUpdateLobby(LobbyRecord lobby) {
        this.lobby = lobby;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateLobby(lobby);
    }
}
