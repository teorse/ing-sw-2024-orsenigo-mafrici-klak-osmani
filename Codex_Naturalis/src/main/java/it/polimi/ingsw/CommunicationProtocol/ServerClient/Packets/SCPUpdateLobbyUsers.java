package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
