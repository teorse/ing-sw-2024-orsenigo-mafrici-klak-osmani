package Network.ClientServer.Packets;

import Model.Game.CardPoolTypes;
import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPDrawCard implements ClientServerPacket{
    private final CardPoolTypes cardPoolType;
    private final int cardIndex;

    public CSPDrawCard(CardPoolTypes cardPoolType, int cardIndex) {
        this.cardPoolType = cardPoolType;
        this.cardIndex = cardIndex;
    }

    @Override
    public void execute(ServerInputExecutor executor) {
        executor.drawCard(cardPoolType, cardIndex);
    }
}
