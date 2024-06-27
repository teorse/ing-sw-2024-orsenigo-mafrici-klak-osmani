package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to update a card map belonging to a specific user.
 * Contains the username of the owner and the updated {@code CardMapRecord}.
 * Upon receiving this packet, the client invokes {@code updateCardMap} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateCardMap implements ServerClientPacket{
    private final String ownerUsername;
    private final CardMapRecord cardMap;

    /**
     * Constructs a {@code SCPUpdateCardMap} instance with the specified owner username and card map record.
     *
     * @param ownerUsername The username of the owner of the card map.
     * @param cardMap       The updated {@code CardMapRecord} containing the card map details.
     */
    public SCPUpdateCardMap(String ownerUsername, CardMapRecord cardMap) {
        this.ownerUsername = ownerUsername;
        this.cardMap = cardMap;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateCardMap} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method informs the client about the updated card map of a specific user.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized card map and owner username to the client controller
        clientController.updateCardMap(ownerUsername, cardMap);
    }
}
