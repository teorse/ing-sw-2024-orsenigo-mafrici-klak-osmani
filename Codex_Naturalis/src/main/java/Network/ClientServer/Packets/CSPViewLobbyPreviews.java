package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPViewLobbyPreviews implements ClientServerPacket{
    @Override
    public void execute(ServerInputExecutor executor) {
        executor.viewLobbyPreviews();
    }
}
