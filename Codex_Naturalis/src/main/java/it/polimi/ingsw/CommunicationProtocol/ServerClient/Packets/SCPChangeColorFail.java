package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPChangeColorFail implements ServerClientPacket{
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.changeColorFailed();
    }
}
