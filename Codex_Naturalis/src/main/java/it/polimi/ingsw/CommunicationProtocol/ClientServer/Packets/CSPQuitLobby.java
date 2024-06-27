package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to request quitting the current lobby.
 * This packet informs the server that the client wishes to leave the lobby they are currently in.
 */
public class CSPQuitLobby implements ClientServerPacket{

    /**
     * Executes the operation on the server to process the lobby quitting request.
     * This method invokes the server's executor to handle the request for the client to quit the current lobby.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.quitLobby();
    }
}
