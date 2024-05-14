package Network.ServerClient.Packets;

import Client.Model.Records.LobbyRecord;
import Client.Model.Records.LobbyUserRecord;
import Network.ServerClient.ServerMessageExecutor;

import java.util.List;

public class SCPJoinLobbySuccessful implements ServerClientPacket {
    private final LobbyRecord lobbyRecord;
    private final List<LobbyUserRecord> lobbyUsers;

    public SCPJoinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers){
        this.lobbyRecord = lobbyRecord;
        this.lobbyUsers = lobbyUsers;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.joinLobbySuccessful(lobbyRecord, lobbyUsers);
    }
}
