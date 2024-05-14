package Network.ServerClient.Packets;

import Network.ServerClient.ServerMessageExecutor;

public class SCPLogInSuccess implements ServerClientPacket{

    private final String username;

    public SCPLogInSuccess(String username) {
        this.username = username;
    }


    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.loginSuccess(username);
    }
}
