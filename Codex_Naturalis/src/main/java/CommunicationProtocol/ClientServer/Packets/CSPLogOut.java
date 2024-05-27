package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPLogOut implements ClientServerPacket{

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.logOut();
    }
}
