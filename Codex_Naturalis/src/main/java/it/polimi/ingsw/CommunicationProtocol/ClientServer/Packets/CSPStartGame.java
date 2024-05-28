package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPStartGame implements ClientServerPacket{
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.startGame();
    }
}
