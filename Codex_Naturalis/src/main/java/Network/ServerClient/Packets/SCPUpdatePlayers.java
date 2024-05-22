package Network.ServerClient.Packets;

import Client.Model.Records.CardMapRecord;
import Client.Model.Records.PlayerRecord;
import Network.ServerClient.ServerMessageExecutor;

import java.util.List;
import java.util.Map;

public class SCPUpdatePlayers implements ServerClientPacket{
    private final List<PlayerRecord> players;
    private final Map<String, CardMapRecord> cardMaps;

    public SCPUpdatePlayers(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps) {
        this.players = players;
        this.cardMaps = cardMaps;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        // Pass the deserialized players map to the client controller
        clientController.updatePlayers(players, cardMaps);
    }
}
