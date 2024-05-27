package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

public class SCPJoinLobbySuccessful implements ServerClientPacket {
    private final LobbyRecord lobbyRecord;
    private final List<LobbyUserRecord> lobbyUsers;

    public SCPJoinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers){
        this.lobbyRecord = lobbyRecord;
        this.lobbyUsers = lobbyUsers;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.joinLobbySuccessful(lobbyRecord, lobbyUsers);
    }
}
