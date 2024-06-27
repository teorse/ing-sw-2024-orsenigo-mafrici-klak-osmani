package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;
import java.util.Map;

/**
 * Packet sent from the server to the client to update the list of players and their associated card maps.
 * Contains a list of {@code PlayerRecord} objects representing players and a map of {@code CardMapRecord}
 * objects representing card maps associated with players.
 * Upon receiving this packet, the client invokes {@code updatePlayers} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdatePlayers implements ServerClientPacket{
    private final List<PlayerRecord> players;
    private final Map<String, CardMapRecord> cardMaps;

    /**
     * Constructs a {@code SCPUpdatePlayers} instance with the specified list of players and their associated card maps.
     *
     * @param players  The list of player records to be updated on the client side.
     * @param cardMaps The map of card map records associated with players to be updated on the client side.
     */
    public SCPUpdatePlayers(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps) {
        this.players = players;
        this.cardMaps = cardMaps;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updatePlayers} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the list of players and their associated card maps
     *                         with the information received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized players map to the client controller
        clientController.updatePlayers(players, cardMaps);
    }
}
