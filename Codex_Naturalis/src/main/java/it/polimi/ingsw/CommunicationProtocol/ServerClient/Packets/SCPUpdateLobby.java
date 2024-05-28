package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPUpdateLobby implements ServerClientPacket{

    private final LobbyRecord lobby;

    public SCPUpdateLobby(LobbyRecord lobby) {
        this.lobby = lobby;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateLobby(lobby);
    }
}
