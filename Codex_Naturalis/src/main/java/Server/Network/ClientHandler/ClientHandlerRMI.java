package Server.Network.ClientHandler;

import Network.ServerClientPacket.ServerClientPacket;
import Server.Controller.InputHandler.InputHandler;


//todo implement RMI logic

/**
 * ClientHandlerRMI class represents the RMI implementation of the ClientHandler interface.
 * It handles communication with a client over an RMI connection.
 */
public class ClientHandlerRMI implements ClientHandler, Runnable{
    /**
     * {@inheritDoc}
     * @param packet The packet to be sent to the client.
     */
    @Override
    public void sendPacket(ServerClientPacket packet) {

    }

    /**
     * {@inheritDoc}
     * @param inputHandler The InputHandler object to be set for the client.
     */
    @Override
    public void setInputHandler(InputHandler inputHandler) {

    }

    @Override
    public void run() {

    }
}
