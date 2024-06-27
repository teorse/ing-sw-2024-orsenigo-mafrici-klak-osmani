package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.*;

import java.util.List;
import java.util.Map;

/**
 * Packet sent from the server to the client to signal the start of a game session.
 * Contains initial game state information such as players, card maps, player secrets,
 * table state, and general game information.
 * Upon receiving this packet, the client invokes {@code gameStarted} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPGameStarted implements ServerClientPacket{
    private final List<PlayerRecord> players;
    private final Map<String, CardMapRecord> cardMaps;
    private final PlayerSecretInfoRecord secret;
    private final TableRecord table;
    private final GameRecord game;

    /**
     * Constructs a {@code SCPGameStarted} instance with the provided initial game state information.
     *
     * @param players  List of player records participating in the game.
     * @param cardMaps Map of card maps associated with each player.
     * @param secret   Player secret information record.
     * @param table    Table record representing the state of the game table.
     * @param game     Game record containing general information about the game.
     */
    public SCPGameStarted(List<PlayerRecord> players, Map<String, CardMapRecord> cardMaps, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game){
        this.players = players;
        this.cardMaps = cardMaps;
        this.secret = secret;
        this.table = table;
        this.game = game;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code gameStarted} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method notifies the client about the start of the game and provides initial state information.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.gameStarted(players, cardMaps, secret, table, game);
    }
}
