package Network.ServerClient.Packets;

import Client.Model.Records.ObjectiveRecord;
import Network.ServerClient.ServerMessageExecutor;

import java.util.List;

public class SCPUpdateSecretObjectiveCandidates implements ServerClientPacket {

    private final List<ObjectiveRecord> candidates;

    public SCPUpdateSecretObjectiveCandidates(List<ObjectiveRecord> candidates) {
        this.candidates = candidates;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.updateSecretObjectiveCandidates(candidates);
    }
}
