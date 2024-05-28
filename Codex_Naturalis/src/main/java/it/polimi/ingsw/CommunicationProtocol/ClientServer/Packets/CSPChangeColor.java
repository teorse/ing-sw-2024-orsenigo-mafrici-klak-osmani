package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

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
