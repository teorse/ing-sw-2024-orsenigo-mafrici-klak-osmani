package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPQuitLobby implements ClientServerPacket{
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.quitLobby();
    }
}
