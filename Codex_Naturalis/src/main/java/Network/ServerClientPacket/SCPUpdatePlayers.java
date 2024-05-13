package Network.ServerClientPacket;

import Client.Model.Records.CardMapRecord;
import Client.Model.Records.PlayerRecord;
import Network.ServerMessageExecutor;

import java.util.Map;

public class SCPUpdatePlayers implements ServerClientPacket{
    private final Map<PlayerRecord, CardMapRecord> players;

    public SCPUpdatePlayers(Map<PlayerRecord, CardMapRecord> players) {
        this.players = players;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        // Pass the deserialized players map to the client controller
        clientController.updatePlayers(players);
    }
}
