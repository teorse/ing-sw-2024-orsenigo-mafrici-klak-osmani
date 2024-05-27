package Server.Controller.InputHandler;

import CommunicationProtocol.ClientServer.Packets.ClientServerPacket;

/**
 * The InputHandler interface defines the contract for classes that handle input messages.<br>
 * They act as middleware between the ClientHandler and the Controllers, they parse serverUser inputs and either further
 * the handling of the input to inputHandlers in the lower levels or if they are the target level they will execute the
 * requested lobbyController method.<br>
 * Classes implementing this interface are responsible for processing incoming messages from clients.
 */
public interface InputHandler{
    /**
     * Handles the input message received from the client.
     *
     * @param packet The ClientServerPacket representing the input from the client.
     */
    void handleInput(ClientServerPacket packet);

    /**
     * Handles the accidental disconnection of the client.
     */
    void clientDisconnectionProcedure();
}
