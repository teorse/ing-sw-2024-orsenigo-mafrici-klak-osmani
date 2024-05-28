package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerSecretInfoRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
