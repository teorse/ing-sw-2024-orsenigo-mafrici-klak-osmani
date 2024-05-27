package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized players map to the client controller
        clientController.updatePlayers(players, cardMaps);
    }
}
