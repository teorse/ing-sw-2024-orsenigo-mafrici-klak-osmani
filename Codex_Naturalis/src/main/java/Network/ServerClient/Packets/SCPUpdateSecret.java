package Network.ServerClient.Packets;

import Client.Model.Records.PlayerSecretInfoRecord;
import Network.ServerMessageExecutor;

public class SCPUpdateSecret implements ServerClientPacket{
    private final PlayerSecretInfoRecord secret;

    public SCPUpdateSecret(PlayerSecretInfoRecord secret) {
        this.secret = secret;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        // Pass the deserialized secret info record to the client controller
        clientController.updateSecret(secret);
    }
}
