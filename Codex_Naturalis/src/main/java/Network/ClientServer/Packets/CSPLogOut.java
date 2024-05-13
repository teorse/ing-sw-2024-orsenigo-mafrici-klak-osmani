package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPLogOut implements ClientServerPacket{

    @Override
    public void execute(ServerInputExecutor executor) {
        executor.logOut();
    }
}
