package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

public class SCPGameOver implements ServerClientPacket {

    /**
     * List of players sorted by winners first losers last
     */
    private final List<PlayerRecord> players;

    public SCPGameOver(List<PlayerRecord> players) {
        this.players = players;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.gameOver(players);
    }
}
