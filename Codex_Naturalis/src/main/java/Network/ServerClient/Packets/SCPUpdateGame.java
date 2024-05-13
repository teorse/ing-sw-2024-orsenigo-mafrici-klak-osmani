package Network.ServerClient.Packets;

import Client.Model.Records.GameRecord;
import Network.ServerMessageExecutor;

public class SCPUpdateGame implements ServerClientPacket {
    private final GameRecord game;

    public SCPUpdateGame(GameRecord game) {
        this.game = game;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        // Pass the deserialized game record to the client controller
        clientController.updateGame(game);
    }
}
