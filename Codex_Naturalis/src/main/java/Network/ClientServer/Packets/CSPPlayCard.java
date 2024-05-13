package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

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
    public void execute(ServerInputExecutor executor) {
        executor.playCard(cardIndex, coordinateIndex, faceUp);
    }
}
