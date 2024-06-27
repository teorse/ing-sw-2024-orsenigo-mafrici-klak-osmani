package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

/**
 * Packet sent from the client to the server to indicate the selection of a secret objective.
 * Contains the index of the objective selected by the client.
 */
public class CSPPickObjective implements ClientServerPacket{
    private final int objectiveIndex;

    /**
     * Constructs a CSPPickObjective packet with the specified objective index.
     *
     * @param objectiveIndex The index of the objective selected by the client.
     */
    public CSPPickObjective(int objectiveIndex) {
        this.objectiveIndex = objectiveIndex;
    }

    /**
     * Executes the operation on the server to process the selection of the secret objective.
     * This method invokes the server's executor to handle the objective selection with the provided index.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.pickObjective(objectiveIndex);
    }
}
