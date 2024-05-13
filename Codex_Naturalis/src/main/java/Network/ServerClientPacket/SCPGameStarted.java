package Network.ServerClientPacket;

import Client.Model.Records.*;
import Network.ServerMessageExecutor;

import java.util.Map;

public class SCPGameStarted implements ServerClientPacket{
    private final Map<PlayerRecord, CardMapRecord> players;
    private final PlayerSecretInfoRecord secret;
    private final TableRecord table;
    private final GameRecord game;

    public SCPGameStarted(Map<PlayerRecord, CardMapRecord> players, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game){
        this.players = players;
        this.secret = secret;
        this.table = table;
        this.game = game;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.gameStarted(players, secret, table, game);
    }
}
