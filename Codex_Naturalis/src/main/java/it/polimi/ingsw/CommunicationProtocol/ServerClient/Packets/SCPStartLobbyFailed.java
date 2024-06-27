package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client indicating a failed attempt to start a lobby.
 * Contains the specific error cause represented by {@code ErrorsDictionary}.
 * Upon receiving this packet, the client invokes {@code startLobbyFailed} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPStartLobbyFailed implements ServerClientPacket{

    private final ErrorsDictionary errorCause;

    /**
     * Constructs a {@code SCPStartLobbyFailed} instance with the specified error cause.
     *
     * @param errorCause The specific error cause represented by {@code ErrorsDictionary}.
     */
    public SCPStartLobbyFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code startLobbyFailed} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method informs the client about the failed attempt to start a lobby,
     *                         providing the specific error cause.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.startLobbyFailed(errorCause);
    }
}
