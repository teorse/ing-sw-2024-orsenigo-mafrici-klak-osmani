package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to request playing a card on a coordinate.
 * Contains information about the card index, coordinate index, and whether the card should be played face up or face down.
 */
public class CSPPlayCard implements ClientServerPacket{
    private final int cardIndex;
    private final int coordinateIndex;
    private final boolean faceUp;

    /**
     * Constructs a CSPPlayCard packet with the specified parameters.
     *
     * @param cardIndex       The index of the card to be played.
     * @param coordinateIndex The index of the coordinate on which to play the card.
     * @param faceUp          Indicates whether the card should be played face up (true) or face down (false).
     */
    public CSPPlayCard(int cardIndex, int coordinateIndex, boolean faceUp) {
        this.cardIndex = cardIndex;
        this.coordinateIndex = coordinateIndex;
        this.faceUp = faceUp;
    }

    /**
     * Executes the operation on the server to process the card play request.
     * This method invokes the server's executor to handle the request to play a card with the specified details.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.playCard(cardIndex, coordinateIndex, faceUp);
    }
}
