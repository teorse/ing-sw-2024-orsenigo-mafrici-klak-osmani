package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

/**
 * Packet sent from the server to the client to signal the end of the game and provide the final player standings.
 * Upon receiving this packet, the client invokes {@code gameOver(List<PlayerRecord> players)} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPGameOver implements ServerClientPacket {

    /**
     * List of players sorted by winners first losers last
     */
    private final List<PlayerRecord> players;

    /**
     * Constructs a {@code SCPGameOver} instance with the provided list of player records.
     *
     * @param players The list of player records representing the final standings of the game.
     */
    public SCPGameOver(List<PlayerRecord> players) {
        this.players = players;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code gameOver(List<PlayerRecord> players)}.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method notifies the client about the end of the game and provides the final standings.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.gameOver(players);
    }
}
