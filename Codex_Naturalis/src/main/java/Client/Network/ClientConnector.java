package Client.Network;

import Network.ClientServerPacket.ClientServerPacket;

/**
 * The ClientConnector interface represents the client-side component responsible for establishing a connection with the server,
 * listening for packets from the server, and sending packets to the server.
 */
public interface ClientConnector extends Runnable {

    /**
     * Sends a packet to the server.
     *
     * @param packet The packet to be sent to the server.
     */
    void sendPacket(ClientServerPacket packet);
}
