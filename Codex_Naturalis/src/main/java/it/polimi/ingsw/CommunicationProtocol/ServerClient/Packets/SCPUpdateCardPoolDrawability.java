package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.Map;

/**
 * Packet sent from the server to the client to update the drawability status of card pools.
 * Contains a map associating each {@code CardPoolTypes} with a boolean indicating its drawability status.
 * Upon receiving this packet, the client invokes {@code updateCardPoolDrawability} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateCardPoolDrawability implements ServerClientPacket{

    private final Map<CardPoolTypes, Boolean> cardPoolDrawability;

    /**
     * Constructs a {@code SCPUpdateCardPoolDrawability} instance with the specified card pool drawability map.
     *
     * @param cardPoolDrawability The map associating each {@code CardPoolTypes} with its drawability status.
     */
    public SCPUpdateCardPoolDrawability(Map<CardPoolTypes, Boolean> cardPoolDrawability) {
        this.cardPoolDrawability = cardPoolDrawability;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateCardPoolDrawability} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the drawability status of card pools based on the received map.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateCardPoolDrawability(cardPoolDrawability);
    }
}
