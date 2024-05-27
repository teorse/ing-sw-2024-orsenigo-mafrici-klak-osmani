package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPChangeColorFail implements ServerClientPacket{
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.changeColorFailed();
    }
}
