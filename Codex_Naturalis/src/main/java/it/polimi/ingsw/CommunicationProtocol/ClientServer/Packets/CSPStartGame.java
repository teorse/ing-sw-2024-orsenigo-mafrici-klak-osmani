package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to request starting a game.
 * This packet triggers the server to initiate the game setup and start.
 */
public class CSPStartGame implements ClientServerPacket{

    /**
     * Packet sent from the client to the server to request starting a game.
     * This packet triggers the server to initiate the game setup and start.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.startGame();
    }
}
