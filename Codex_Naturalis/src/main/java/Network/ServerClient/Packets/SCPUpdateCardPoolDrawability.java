package Network.ServerClient.Packets;

import Client.Model.Records.CardPoolDrawabilityRecord;
import Network.ServerClient.ServerMessageExecutor;

public class SCPUpdateCardPoolDrawability implements ServerClientPacket{

    private final CardPoolDrawabilityRecord cardPoolDrawability;

    public SCPUpdateCardPoolDrawability(CardPoolDrawabilityRecord cardPoolDrawability) {
        this.cardPoolDrawability = cardPoolDrawability;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.updateCardPoolDrawability(cardPoolDrawability);
    }
}
