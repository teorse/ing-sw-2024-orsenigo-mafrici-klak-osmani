package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.GameRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPUpdateGame implements ServerClientPacket {
    private final GameRecord game;

    public SCPUpdateGame(GameRecord game) {
        this.game = game;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized game record to the client controller
        clientController.updateGame(game);
    }
}
