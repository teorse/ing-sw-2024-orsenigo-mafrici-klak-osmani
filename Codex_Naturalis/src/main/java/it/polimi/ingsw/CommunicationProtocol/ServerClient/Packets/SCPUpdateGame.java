package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.GameRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
