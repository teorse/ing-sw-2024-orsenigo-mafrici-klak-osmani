package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPLogOut implements ClientServerPacket{

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.logOut();
    }
}
