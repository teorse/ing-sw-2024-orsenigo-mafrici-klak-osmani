package CommunicationProtocol.ClientServer.Packets;

import CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

import java.io.Serial;

public class CSPSignUp implements ClientServerPacket{
    @Serial
    private static final long serialVersionUID = 7493033446193398063L;
    private final String username;
    private final String password;

    public CSPSignUp(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.signUp(username, password);
    }
}
