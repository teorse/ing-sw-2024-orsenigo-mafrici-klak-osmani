package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
