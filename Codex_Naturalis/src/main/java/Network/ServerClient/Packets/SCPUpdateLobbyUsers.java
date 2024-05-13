package Network.ServerClient.Packets;

import Client.Model.Records.LobbyUserRecord;
import Network.ServerMessageExecutor;

import java.util.List;

public class SCPUpdateLobbyUsers implements ServerClientPacket{
    private final List<LobbyUserRecord> lobbyUsers;

    public SCPUpdateLobbyUsers(List<LobbyUserRecord> lobbyUsers){
        this.lobbyUsers = lobbyUsers;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.updateLobbyUsers(lobbyUsers);
    }
}
