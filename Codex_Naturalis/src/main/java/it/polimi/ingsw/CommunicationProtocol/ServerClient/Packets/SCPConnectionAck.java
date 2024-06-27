package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to acknowledge a successful connection.
 * Upon receiving this packet, the client invokes {@code connectionAck(String message)} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPConnectionAck implements ServerClientPacket{

    private final String message;

    /**
     * Constructs a {@code SCPConnectionAck} instance with the specified message.
     *
     * @param message The acknowledgment message sent from the server to the client.
     */
    public SCPConnectionAck(String message) {
        this.message = message;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code connectionAck(String message)}.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                      This method notifies the client about a successful connection with a specific message.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.connectionAck(message);
    }
}
