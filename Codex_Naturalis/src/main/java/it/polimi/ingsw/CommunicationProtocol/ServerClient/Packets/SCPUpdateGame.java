package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.GameRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to update the game state.
 * Contains the {@code GameRecord} encapsulating details about the current game state.
 * Upon receiving this packet, the client invokes {@code updateGame} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateGame implements ServerClientPacket {
    private final GameRecord game;

    /**
     * Constructs a {@code SCPUpdateGame} instance with the specified game record.
     *
     * @param game The game record to be updated on the client side.
     */
    public SCPUpdateGame(GameRecord game) {
        this.game = game;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateGame} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the game state with the information received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized game record to the client controller
        clientController.updateGame(game);
    }
}
