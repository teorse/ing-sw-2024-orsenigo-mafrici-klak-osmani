package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to request viewing lobby previews.
 * This packet instructs the server to send lobby preview information to the client.
 */
public class CSPViewLobbyPreviews implements ClientServerPacket{

    /**
     * Executes the operation on the server to start viewing lobby previews.
     * This method invokes the server's executor to handle the request for lobby preview updates.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.viewLobbyPreviews();
    }
}
