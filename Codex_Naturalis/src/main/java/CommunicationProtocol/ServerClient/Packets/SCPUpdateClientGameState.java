package CommunicationProtocol.ServerClient.Packets;

import Server.Model.Game.Player.PlayerStates;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
