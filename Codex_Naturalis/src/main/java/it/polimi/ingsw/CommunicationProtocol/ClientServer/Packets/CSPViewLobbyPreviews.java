package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPViewLobbyPreviews implements ClientServerPacket{
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.viewLobbyPreviews();
    }
}
