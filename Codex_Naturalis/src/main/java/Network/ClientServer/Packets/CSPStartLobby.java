package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPStartLobby implements ClientServerPacket{
    private final String lobbyName;
    private final int lobbySize;

    public CSPStartLobby(String lobbyName, int lobbySize) {
        this.lobbyName = lobbyName;
        this.lobbySize = lobbySize;
    }


    @Override
    public void execute(ServerInputExecutor executor) {
        executor.startLobby(lobbyName, lobbySize);
    }
}
