package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client indicating a successful sign-up attempt.
 * Contains the username of the client who successfully signed up.
 * Upon receiving this packet, the client invokes {@code signUpSuccess} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPSignUpSuccess implements ServerClientPacket{

    private final String username;

    /**
     * Constructs a {@code SCPSignUpSuccess} instance with the specified username.
     *
     * @param username The username of the client who successfully signed up.
     */
    public SCPSignUpSuccess(String username) {
        this.username = username;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code signUpSuccess} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method informs the client about the successful sign-up attempt with the username.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.signUpSuccess(username);
    }
}
