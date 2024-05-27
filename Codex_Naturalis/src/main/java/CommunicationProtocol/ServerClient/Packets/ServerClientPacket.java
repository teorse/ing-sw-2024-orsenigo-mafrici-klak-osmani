package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.io.Serializable;

/**
 * The ServerClientPacket interface represents packets sent from the server to the client.<br>
 * It implements the Command pattern and declares the execute method that the client will run
 * after receiving the packet to allow it to update the appropriate classes/attributes in the model/view of the client.
 */
public interface ServerClientPacket extends Serializable {

    /**
     * Executes the packet on the client side.
     *
     * @param clientController The ClientController object on the client side to execute the message.
     */
    void execute(ServerClientMessageExecutor clientController);
}
