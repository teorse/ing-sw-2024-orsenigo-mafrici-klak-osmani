package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to notify that the attempt to change color has failed.
 * Upon receiving this packet, the client invokes {@code changeColorFailed()} method on the {@code ServerClientMessageExecutor}.
 */

public class SCPChangeColorFail implements ServerClientPacket{

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code changeColorFailed()}.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                      This method notifies the client that the attempt to change color has failed.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.changeColorFailed();
    }
}
