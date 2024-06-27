package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

/**
 * Packet sent from the client to the server to change the user's lobby color.
 * This packet contains information about the new lobby color selected by the client.
 */
public class CSPChangeColor implements ClientServerPacket{
    private final LobbyUserColors newColor;

    /**
     * Constructs a CSPChangeColor packet with the specified new lobby color.
     *
     * @param newColor The new lobby color chosen by the client.
     */
    public CSPChangeColor(LobbyUserColors newColor) {
        this.newColor = newColor;
    }

    /**
     * Executes the change color operation on the server.
     * This method invokes the server's executor to process the change color request,
     * updating the user's lobby color to the specified new color.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.changeColor(newColor);
    }
}
