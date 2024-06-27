package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

/**
 * Packet sent from the server to the client to update the secret objective candidates of a player.
 * Contains a list of {@code ObjectiveRecord} objects representing the updated secret objective candidates.
 * Upon receiving this packet, the client invokes {@code updateSecretObjectiveCandidates} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateSecretObjectiveCandidates implements ServerClientPacket {

    private final List<ObjectiveRecord> candidates;

    /**
     * Constructs an {@code SCPUpdateSecretObjectiveCandidates} instance with the specified list of objective candidates.
     *
     * @param candidates The list of secret objective candidates to be updated on the client side.
     */
    public SCPUpdateSecretObjectiveCandidates(List<ObjectiveRecord> candidates) {
        this.candidates = candidates;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateSecretObjectiveCandidates} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the secret objective candidates of a player with the information received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateSecretObjectiveCandidates(candidates);
    }
}
