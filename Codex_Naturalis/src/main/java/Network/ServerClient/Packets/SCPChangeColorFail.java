package Network.ServerClient.Packets;

import Network.ServerClient.ServerMessageExecutor;

public class SCPChangeColorFail implements ServerClientPacket{
    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.changeColorFailed();
    }
}
