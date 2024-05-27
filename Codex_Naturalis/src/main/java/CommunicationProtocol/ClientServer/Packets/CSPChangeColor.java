package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;
import Server.Model.Lobby.LobbyUserColors;

public class CSPChangeColor implements ClientServerPacket{
    private final LobbyUserColors newColor;

    public CSPChangeColor(LobbyUserColors newColor) {
        this.newColor = newColor;
    }

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.changeColor(newColor);
    }
}
