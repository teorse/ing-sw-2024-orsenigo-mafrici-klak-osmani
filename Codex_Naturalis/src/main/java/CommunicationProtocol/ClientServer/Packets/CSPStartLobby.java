package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPStartLobby implements ClientServerPacket{
    private final String lobbyName;
    private final int lobbySize;

    public CSPStartLobby(String lobbyName, int lobbySize) {
        this.lobbyName = lobbyName;
        this.lobbySize = lobbySize;
    }


    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.startLobby(lobbyName, lobbySize);
    }
}
