package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

import java.io.Serial;

public class CSPLogIn implements ClientServerPacket{
    @Serial
    private static final long serialVersionUID = 6226599305397317241L;
    private final String username;
    private final String password;

    public CSPLogIn(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Override
    public void execute(ServerInputExecutor executor) {
        executor.logIn(username, password);
    }
}