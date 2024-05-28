package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPUpdateSpecificPlayer implements ServerClientPacket{
    private final PlayerRecord player;

    public SCPUpdateSpecificPlayer(PlayerRecord player) {
        this.player = player;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized player record to the client controller
        clientController.updateSpecificPlayer(player);
    }
}
