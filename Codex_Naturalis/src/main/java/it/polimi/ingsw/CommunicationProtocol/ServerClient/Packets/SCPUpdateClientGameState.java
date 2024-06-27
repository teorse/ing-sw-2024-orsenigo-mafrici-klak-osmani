package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to update the state of a player.
 * Contains the next {@code PlayerStates} indicating the state transition.
 * Upon receiving this packet, the client invokes {@code updateClientGameState} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateClientGameState implements ServerClientPacket{

    private final PlayerStates nextState;

    /**
     * Constructs a {@code SCPUpdateClientGameState} instance with the specified next state.
     *
     * @param nextState The next state of the player to be updated on the client side.
     */
    public SCPUpdateClientGameState(PlayerStates nextState) {
        this.nextState = nextState;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateClientGameState} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the state of the player to the next state received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.updateClientGameState(nextState);
    }
}
