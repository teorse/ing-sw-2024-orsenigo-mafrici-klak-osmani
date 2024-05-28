package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.List;

public class SCPUpdateSecretObjectiveCandidates implements ServerClientPacket {

    private final List<ObjectiveRecord> candidates;

    public SCPUpdateSecretObjectiveCandidates(List<ObjectiveRecord> candidates) {
        this.candidates = candidates;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateSecretObjectiveCandidates(candidates);
    }
}
