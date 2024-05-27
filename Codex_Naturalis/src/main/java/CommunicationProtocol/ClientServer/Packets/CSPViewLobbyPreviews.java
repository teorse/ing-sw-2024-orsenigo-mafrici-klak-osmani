package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPViewLobbyPreviews implements ClientServerPacket{
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.viewLobbyPreviews();
    }
}
