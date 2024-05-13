package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;
import Server.Model.Lobby.LobbyUserColors;

public class CSPChangeColor implements ClientServerPacket{
    private final LobbyUserColors newColor;

    public CSPChangeColor(LobbyUserColors newColor) {
        this.newColor = newColor;
    }

    @Override
    public void execute(ServerInputExecutor executor) {
        executor.changeColor(newColor);
    }
}
