package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

import java.io.Serial;

/**
 * Packet sent from the client to the server to request a login operation.
 * This packet contains the username and password for authentication.
 */
public class CSPLogIn implements ClientServerPacket{
    @Serial
    private static final long serialVersionUID = 6226599305397317241L;
    private final String username;
    private final String password;

    /**
     * Constructs a CSPLogIn packet with the specified username and password.
     *
     * @param username The username used for authentication.
     * @param password The password associated with the username.
     */
    public CSPLogIn(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Executes the login operation on the server.
     * This method invokes the server's executor to process the login request,
     * attempting to authenticate the client with the provided username and password.
     *
     * @param executor The executor responsible for handling messages on the server side.
     */
    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.logIn(username, password);
    }
}
