package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to request a logout operation.
 * This packet does not contain any additional data beyond the request itself.
 */
public class CSPLogOut implements ClientServerPacket{

    /**
     * Executes the logout operation on the server.
     * This method invokes the server's executor to process the logout request,
     * indicating that the client wishes to terminate its session.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.logOut();
    }
}
