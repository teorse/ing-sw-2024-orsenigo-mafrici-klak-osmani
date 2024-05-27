package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

public class SCPUpdateLobbyUsers implements ServerClientPacket{
    private final List<LobbyUserRecord> lobbyUsers;

    public SCPUpdateLobbyUsers(List<LobbyUserRecord> lobbyUsers){
        this.lobbyUsers = lobbyUsers;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateLobbyUsers(lobbyUsers);
    }
}
