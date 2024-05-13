package Network.ServerClient.Packets;

import Client.Model.Records.LobbyRecord;
import Client.Model.Records.LobbyUserRecord;
import Network.ServerMessageExecutor;

import java.util.List;

public class SCPJoinLobbySuccessful implements ServerClientPacket {
    private final LobbyRecord lobbyRecord;
    private final List<LobbyUserRecord> lobbyUsers;
    private final String notification;

    public SCPJoinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers, String notification){
        this.lobbyRecord = lobbyRecord;
        this.lobbyUsers = lobbyUsers;
        this.notification = notification;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.joinLobbySuccessful(lobbyRecord, lobbyUsers, notification);
    }
}
