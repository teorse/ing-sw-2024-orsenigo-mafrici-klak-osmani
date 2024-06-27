package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to request joining a specific lobby.
 * This packet contains the name of the lobby that the client wants to join.
 */
public class CSPJoinLobby implements ClientServerPacket{
    private final String lobbyName;

    /**
     * Constructs a CSPJoinLobby packet with the specified lobby name.
     *
     * @param lobbyName The name of the lobby that the client wants to join.
     */
    public CSPJoinLobby(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    /**
     * Executes the join lobby operation on the server.
     * This method invokes the server's executor to process the join lobby request,
     * attempting to add the client to the specified lobby.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.joinLobby(lobbyName);
    }
}
