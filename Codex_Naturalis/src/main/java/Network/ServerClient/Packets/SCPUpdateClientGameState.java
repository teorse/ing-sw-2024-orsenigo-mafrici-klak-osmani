package Network.ServerClient.Packets;

import Model.Player.PlayerStates;
import Network.ServerClient.ServerMessageExecutor;

public class SCPUpdateClientGameState implements ServerClientPacket{

    private final PlayerStates nextState;

    public SCPUpdateClientGameState(PlayerStates nextState) {
        this.nextState = nextState;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.updateClientGameState(nextState);
    }
}
