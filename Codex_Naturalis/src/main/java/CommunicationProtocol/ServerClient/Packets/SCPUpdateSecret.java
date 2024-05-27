package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.PlayerSecretInfoRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPUpdateSecret implements ServerClientPacket{
    private final PlayerSecretInfoRecord secret;

    public SCPUpdateSecret(PlayerSecretInfoRecord secret) {
        this.secret = secret;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized secret info record to the client controller
        clientController.updateSecret(secret);
    }
}
