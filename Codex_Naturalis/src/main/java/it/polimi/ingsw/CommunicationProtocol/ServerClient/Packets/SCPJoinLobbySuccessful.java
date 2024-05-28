package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
