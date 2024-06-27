package it.polimi.ingsw.Server.Network.ClientHandler;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ServerClientPacket;
import it.polimi.ingsw.Server.Controller.InputHandler.InputHandler;

/**
 * The ClientHandler interface defines methods for handling client connections.
 */
public interface ClientHandler extends Runnable {
    /**
     * Sends a message to the connected client.
     *
     * @param packet The packet to be sent to the client.
     */
    void sendPacket(ServerClientPacket packet);
}
