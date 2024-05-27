package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPStartGame implements ClientServerPacket{
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.startGame();
    }
}
