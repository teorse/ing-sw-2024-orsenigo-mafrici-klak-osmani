package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client indicating a failed sign-up attempt.
 * Contains an {@code ErrorsDictionary} object describing the cause of the failure.
 * Upon receiving this packet, the client invokes {@code signUpFailed} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPSignUpFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    /**
     * Constructs a {@code SCPSignUpFailed} instance with the specified {@code ErrorsDictionary}.
     *
     * @param errorCause The dictionary containing details about the sign-up failure.
     */
    public SCPSignUpFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code signUpFailed} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method informs the client about the failed sign-up attempt with specific details.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.signUpFailed(errorCause);
    }
}
