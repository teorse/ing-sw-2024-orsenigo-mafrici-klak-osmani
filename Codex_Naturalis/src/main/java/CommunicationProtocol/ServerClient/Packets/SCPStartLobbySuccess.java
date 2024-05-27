package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.io.Serial;
import java.util.List;

public class SCPStartLobbySuccess implements ServerClientPacket{
    @Serial
    private static final long serialVersionUID = -7181004148327909726L;
    private final LobbyRecord lobbyRecord;
    private final List<LobbyUserRecord> lobbyUsers;

    public SCPStartLobbySuccess(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers) {
        this.lobbyRecord = lobbyRecord;
        this.lobbyUsers = lobbyUsers;
    }


    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.startLobbySuccess(lobbyRecord, lobbyUsers);
    }
}
