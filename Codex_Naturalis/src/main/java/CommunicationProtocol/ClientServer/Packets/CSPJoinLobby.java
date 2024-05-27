package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPJoinLobby implements ClientServerPacket{
    private final String lobbyName;

    public CSPJoinLobby(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.joinLobby(lobbyName);
    }
}
