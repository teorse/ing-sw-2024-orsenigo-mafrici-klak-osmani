package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to indicate a failed login attempt.
 * Contains information about the cause of the failure.
 * Upon receiving this packet, the client invokes {@code loginFailed} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPLogInFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    /**
     * Constructs a {@code SCPLogInFailed} instance with the specified error cause.
     *
     * @param errorCause The {@code ErrorsDictionary} object indicating the reason for the login failure.
     */
    public SCPLogInFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code loginFailed} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method notifies the client about the failed login attempt,
     *                         providing details about the cause of the failure.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.loginFailed(errorCause);
    }
}
