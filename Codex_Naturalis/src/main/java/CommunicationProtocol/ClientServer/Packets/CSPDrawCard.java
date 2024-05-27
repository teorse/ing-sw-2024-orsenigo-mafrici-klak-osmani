package CommunicationProtocol.ClientServer.Packets;

import Server.Model.Game.Table.CardPoolTypes;
import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPDrawCard implements ClientServerPacket{
    private final CardPoolTypes cardPoolType;
    private final int cardIndex;

    public CSPDrawCard(CardPoolTypes cardPoolType, int cardIndex) {
        this.cardPoolType = cardPoolType;
        this.cardIndex = cardIndex;
    }

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.drawCard(cardPoolType, cardIndex);
    }
}
