package Network.ServerClient.Packets;

import Client.Model.Records.LobbyRecord;
import Client.Model.Records.LobbyUserRecord;
import Network.ServerMessageExecutor;

import java.util.List;

public class SCPStartLobbySuccess implements ServerClientPacket{
    private final LobbyRecord lobbyRecord;
    private final List<LobbyUserRecord> lobbyUsers;

    public SCPStartLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        this.lobbyRecord = lobbyRecord;
        this.lobbyUsers = lobbyUsers;
    }


    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.startLobbySuccess(lobbyRecord, lobbyUsers);
    }
}
