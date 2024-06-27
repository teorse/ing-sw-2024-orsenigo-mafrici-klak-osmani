package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to request drawing a card from a specific card pool.
 * This packet contains information about the type of card pool and the index of the card to be drawn.
 */
public class CSPDrawCard implements ClientServerPacket{
    private final CardPoolTypes cardPoolType;
    private final int cardIndex;

    /**
     * Constructs a CSPDrawCard packet with the specified card pool type and card index.
     *
     * @param cardPoolType The type of card pool from which the card should be drawn.
     * @param cardIndex    The index of the card to be drawn from the specified card pool.
     */
    public CSPDrawCard(CardPoolTypes cardPoolType, int cardIndex) {
        this.cardPoolType = cardPoolType;
        this.cardIndex = cardIndex;
    }

    /**
     * Executes the draw card operation on the server.
     * This method invokes the server's executor to process the draw card request,
     * drawing the card from the specified card pool at the given index.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.drawCard(cardPoolType, cardIndex);
    }
}
