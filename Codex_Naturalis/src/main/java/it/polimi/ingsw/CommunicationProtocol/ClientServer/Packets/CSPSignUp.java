package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

import java.io.Serial;

/**
 * Packet sent from the client to the server to sign up a new user.
 * This packet encapsulates the username and password for the new user account.
 */
public class CSPSignUp implements ClientServerPacket{
    @Serial
    private static final long serialVersionUID = 7493033446193398063L;
    private final String username;
    private final String password;

    /**
     * Constructs a CSPSignUp object with the specified username and password.
     *
     * @param username The username for the new user account.
     * @param password The password for the new user account.
     */
    public CSPSignUp(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Executes the operation on the server to process signing up a new user.
     * This method invokes the server's executor to handle the request for signing up
     * with the provided username and password.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.signUp(username, password);
    }
}
