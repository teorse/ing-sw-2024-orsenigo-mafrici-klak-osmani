package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPStopViewingLobbyPreviews implements ClientServerPacket{
    @Override
    public void execute(ServerInputExecutor executor) {
        executor.stopViewingLobbyPreviews();
    }
}
