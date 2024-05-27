package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
