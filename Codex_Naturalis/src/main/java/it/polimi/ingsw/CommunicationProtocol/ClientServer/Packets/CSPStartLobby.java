package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to request starting a new lobby.
 * This packet contains the name and size of the lobby to be created.
 */
public class CSPStartLobby implements ClientServerPacket{
    private final String lobbyName;
    private final int lobbySize;

    /**
     * Constructs a CSPStartLobby packet with the specified lobby name and size.
     *
     * @param lobbyName The name of the lobby to start.
     * @param lobbySize The size (maximum number of players) of the lobby.
     */
    public CSPStartLobby(String lobbyName, int lobbySize) {
        this.lobbyName = lobbyName;
        this.lobbySize = lobbySize;
    }

    /**
     * Executes the operation on the server to start a new lobby.
     * This method invokes the server's executor to handle the request for starting the lobby.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.startLobby(lobbyName, lobbySize);
    }
}
