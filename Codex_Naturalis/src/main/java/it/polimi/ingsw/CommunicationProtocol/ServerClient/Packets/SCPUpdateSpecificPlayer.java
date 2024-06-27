package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to update a specific player's information.
 * Contains a {@code PlayerRecord} object representing the updated player information.
 * Upon receiving this packet, the client invokes {@code updateSpecificPlayer} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateSpecificPlayer implements ServerClientPacket{
    private final PlayerRecord player;

    /**
     * Constructs an {@code SCPUpdateSpecificPlayer} instance with the specified player record.
     *
     * @param player The player record containing the updated player information.
     */
    public SCPUpdateSpecificPlayer(PlayerRecord player) {
        this.player = player;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateSpecificPlayer} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the information of a specific player with the data received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized player record to the client controller
        clientController.updateSpecificPlayer(player);
    }
}
