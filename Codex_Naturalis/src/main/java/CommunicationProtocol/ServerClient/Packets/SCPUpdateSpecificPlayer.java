package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPUpdateSpecificPlayer implements ServerClientPacket{
    private final PlayerRecord player;

    public SCPUpdateSpecificPlayer(PlayerRecord player) {
        this.player = player;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized player record to the client controller
        clientController.updateSpecificPlayer(player);
    }
}
