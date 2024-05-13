package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

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
    public void execute(ServerInputExecutor executor) {
        executor.signUp(username, password);
    }
}
