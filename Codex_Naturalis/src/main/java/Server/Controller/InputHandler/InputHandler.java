package Server.Controller.InputHandler;

import Network.ClientServerPacket.ClientServerPacket;

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
     * @param message The ClientServerPacket representing the input from the client.
     */
    void handleInput(ClientServerPacket message);

    /**
     * Handles the accidental disconnection of the client.
     */
    void clientDisconnectionProcedure();

    /**
     * Handles the voluntary disconnection of the client through a log-out request from the given layer of the server.
     */
    void logOut();

    /**
     * Checks if the input handler is currently bound to the controller of the given layer.
     *
     * @return true if the input handler is bound to the controller, false if the handler is not bound to anything.
     */
    boolean isBound();
}
