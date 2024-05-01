package Client.Network;

import Network.ClientServerPacket.ClientServerPacket;

/**
 * The ServerHandlerRMI class is an implementation of the ServerHandler interface using RMI.<br>
 * It is responsible for establishing a connection with the server, listening for packets from the server,
 * and sending packets to the server.
 */
public class ServerHandlerRMI implements ServerHandler {

    /**
     * {@inheritDoc}
     *
     * @param packet The packet to be sent to the server.
     */
    @Override
    public void sendPacket(ClientServerPacket packet) {

    }

    /**
     * Listens for packets from the server and executes them using the client controller.
     */
    @Override
    public void run() {

    }
}
