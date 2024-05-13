package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPJoinLobby implements ClientServerPacket{
    private final String lobbyName;

    public CSPJoinLobby(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    @Override
    public void execute(ServerInputExecutor executor) {
        executor.joinLobby(lobbyName);
    }
}
