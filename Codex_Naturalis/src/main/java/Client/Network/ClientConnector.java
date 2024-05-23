package Client.Network;

import Network.ClientServer.Packets.ClientServerPacket;

/**
 * The ClientConnector interface represents the client-side component responsible for establishing a connection with the server,
 * listening for packets from the server, and sending packets to the server.
 */
public interface ClientConnector{

    /**
     * Sends a packet to the server.
     *
     * @param packet The packet to be sent to the server.
     */
    void sendPacket(ClientServerPacket packet);
}
