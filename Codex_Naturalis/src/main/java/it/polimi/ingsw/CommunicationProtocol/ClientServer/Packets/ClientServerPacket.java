package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

import java.io.Serializable;

/**
 * The ClientServerPacket class represents a packet sent from the client to the server.
 * It encapsulates communication between the client and the server.
 */
public interface ClientServerPacket extends Serializable{

    /**
     * Executes the handling of this packet on the server side.
     * Implementations of this method define specific actions to be taken when the server
     * receives and processes this packet.
     *
     * @param executor The executor responsible for handling the message on the server.
     */
    void execute(ClientServerMessageExecutor executor);
}
