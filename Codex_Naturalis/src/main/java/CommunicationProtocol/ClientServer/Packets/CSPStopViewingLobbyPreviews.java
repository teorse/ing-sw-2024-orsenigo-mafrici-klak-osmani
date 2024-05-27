package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPStopViewingLobbyPreviews implements ClientServerPacket{
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.stopViewingLobbyPreviews();
    }
}
