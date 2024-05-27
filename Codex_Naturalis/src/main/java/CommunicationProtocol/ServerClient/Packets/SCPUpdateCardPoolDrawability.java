package CommunicationProtocol.ServerClient.Packets;

import Server.Model.Game.Table.CardPoolTypes;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

import java.util.Map;

public class SCPUpdateCardPoolDrawability implements ServerClientPacket{

    private final Map<CardPoolTypes, Boolean> cardPoolDrawability;

    public SCPUpdateCardPoolDrawability(Map<CardPoolTypes, Boolean> cardPoolDrawability) {
        this.cardPoolDrawability = cardPoolDrawability;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateCardPoolDrawability(cardPoolDrawability);
    }
}
