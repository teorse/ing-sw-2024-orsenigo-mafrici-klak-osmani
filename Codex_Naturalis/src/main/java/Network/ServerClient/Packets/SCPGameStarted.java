package Network.ServerClient.Packets;

import Client.Model.Records.*;
import Network.ServerClient.ServerMessageExecutor;

import java.util.List;
import java.util.Map;

public class SCPGameStarted implements ServerClientPacket{
    private final List<PlayerRecord> players;
    private final Map<String, CardMapRecord> cardMaps;
    private final PlayerSecretInfoRecord secret;
    private final TableRecord table;
    private final GameRecord game;

    public SCPGameStarted(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game){
        this.players = players;
        this.cardMaps = cardMaps;
        this.secret = secret;
        this.table = table;
        this.game = game;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.gameStarted(players, cardMaps, secret, table, game);
    }
}
