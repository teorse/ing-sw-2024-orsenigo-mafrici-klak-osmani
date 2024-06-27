package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerSecretInfoRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to update the secret information of a player.
 * Contains a {@code PlayerSecretInfoRecord} object representing the updated secret information.
 * Upon receiving this packet, the client invokes {@code updateSecret} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateSecret implements ServerClientPacket{
    private final PlayerSecretInfoRecord secret;

    /**
     * Constructs an {@code SCPUpdateSecret} instance with the specified secret information record.
     *
     * @param secret The secret information record to be updated on the client side.
     */
    public SCPUpdateSecret(PlayerSecretInfoRecord secret) {
        this.secret = secret;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateSecret} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the secret information of a player with the information received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized secret info record to the client controller
        clientController.updateSecret(secret);
    }
}
