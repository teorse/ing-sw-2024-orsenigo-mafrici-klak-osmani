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

    /**
     * Sets the input handler for the client.<br>
     * Input handlers parse user inputs and act as middleware to interact with controllers.<br>
     * @see InputHandler InputHandlers
     *
     * @param inputHandler The InputHandler object to be set for the client.
     */
    void setInputHandler(InputHandler inputHandler);
}
