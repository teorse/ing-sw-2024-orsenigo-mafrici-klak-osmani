package Network.ServerClient.Packets;

import Client.Model.Records.PlayerRecord;
import Network.ServerMessageExecutor;

public class SCPUpdateSpecificPlayer implements ServerClientPacket{
    private final PlayerRecord player;

    public SCPUpdateSpecificPlayer(PlayerRecord player) {
        this.player = player;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        // Pass the deserialized player record to the client controller
        clientController.updateSpecificPlayer(player);
    }
}
