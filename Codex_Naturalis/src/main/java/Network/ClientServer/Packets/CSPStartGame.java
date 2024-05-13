package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPStartGame implements ClientServerPacket{
    @Override
    public void execute(ServerInputExecutor executor) {
        executor.startGame();
    }
}
