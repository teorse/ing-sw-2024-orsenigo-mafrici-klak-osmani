package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPPlayCard implements ClientServerPacket{
    private final int cardIndex;
    private final int coordinateIndex;
    private final boolean faceUp;

    public CSPPlayCard(int cardIndex, int coordinateIndex, boolean faceUp) {
        this.cardIndex = cardIndex;
        this.coordinateIndex = coordinateIndex;
        this.faceUp = faceUp;
    }

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.playCard(cardIndex, coordinateIndex, faceUp);
    }
}
