package Network.ServerClient.Packets;

import Client.Model.Records.LobbyRecord;
import Network.ServerClient.ServerMessageExecutor;

public class SCPUpdateLobby implements ServerClientPacket{

    private final LobbyRecord lobby;

    public SCPUpdateLobby(LobbyRecord lobby) {
        this.lobby = lobby;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.updateLobby(lobby);
    }
}
