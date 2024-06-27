package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to indicate a successful login.
 * Contains the username of the successfully logged-in user.
 * Upon receiving this packet, the client invokes {@code loginSuccess} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPLogInSuccess implements ServerClientPacket{

    private final String username;

    /**
     * Constructs a {@code SCPLogInSuccess} instance with the specified username.
     *
     * @param username The username of the successfully logged-in user.
     */
    public SCPLogInSuccess(String username) {
        this.username = username;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code loginSuccess} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method notifies the client about the successful login,
     *                         providing the username of the logged-in user.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.loginSuccess(username);
    }
}
