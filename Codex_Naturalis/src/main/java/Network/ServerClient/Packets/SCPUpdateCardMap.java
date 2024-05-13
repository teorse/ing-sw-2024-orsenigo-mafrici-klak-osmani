package Network.ServerClient.Packets;

import Client.Model.Records.CardMapRecord;
import Network.ServerMessageExecutor;

public class SCPUpdateCardMap implements ServerClientPacket{
    private final String ownerUsername;
    private final CardMapRecord cardMap;

    public SCPUpdateCardMap(String ownerUsername, CardMapRecord cardMap) {
        this.ownerUsername = ownerUsername;
        this.cardMap = cardMap;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        // Pass the deserialized card map and owner username to the client controller
        clientController.updateCardMap(ownerUsername, cardMap);
    }
}
