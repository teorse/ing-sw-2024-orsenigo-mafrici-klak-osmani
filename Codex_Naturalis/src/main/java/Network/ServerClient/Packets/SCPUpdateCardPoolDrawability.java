package Network.ServerClient.Packets;

import Server.Model.Game.Game.CardPoolTypes;
import Network.ServerClient.ServerMessageExecutor;

import java.util.Map;

public class SCPUpdateCardPoolDrawability implements ServerClientPacket{

    private final Map<CardPoolTypes, Boolean> cardPoolDrawability;

    public SCPUpdateCardPoolDrawability(Map<CardPoolTypes, Boolean> cardPoolDrawability) {
        this.cardPoolDrawability = cardPoolDrawability;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.updateCardPoolDrawability(cardPoolDrawability);
    }
}
