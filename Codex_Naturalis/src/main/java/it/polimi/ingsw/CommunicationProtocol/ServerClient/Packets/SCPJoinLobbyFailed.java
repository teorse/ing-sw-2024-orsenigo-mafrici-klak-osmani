package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to indicate a failed attempt to join a lobby.
 * Contains an error cause from {@code ErrorsDictionary} that provides details about the failure reason.
 * Upon receiving this packet, the client invokes {@code joinLobbyFailed} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPJoinLobbyFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    /**
     * Constructs a {@code SCPJoinLobbyFailed} instance with the specified error cause.
     *
     * @param errorCause The {@code ErrorsDictionary} object containing details about the join lobby failure.
     */
    public SCPJoinLobbyFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code joinLobbyFailed} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method notifies the client about the failed attempt to join a lobby
     *                         and provides details encapsulated in {@code ErrorsDictionary}.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.joinLobbyFailed(errorCause);
    }
}
