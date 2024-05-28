package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPUpdateClientGameState implements ServerClientPacket{

    private final PlayerStates nextState;

    public SCPUpdateClientGameState(PlayerStates nextState) {
        this.nextState = nextState;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateClientGameState(nextState);
    }
}
