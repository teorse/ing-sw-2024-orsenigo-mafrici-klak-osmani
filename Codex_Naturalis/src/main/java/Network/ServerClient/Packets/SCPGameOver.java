package Network.ServerClient.Packets;

import Client.Model.Records.PlayerRecord;
import Network.ServerMessageExecutor;

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
    public void execute(ServerMessageExecutor clientController) {
        clientController.gameOver(players);
    }
}
