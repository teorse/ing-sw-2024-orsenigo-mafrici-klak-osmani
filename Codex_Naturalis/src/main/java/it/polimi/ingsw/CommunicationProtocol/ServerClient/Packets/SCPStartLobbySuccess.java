package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
