package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPQuitLobby implements ClientServerPacket{
    @Override
    public void execute(ServerInputExecutor executor) {
        executor.quitLobby();
    }
}
