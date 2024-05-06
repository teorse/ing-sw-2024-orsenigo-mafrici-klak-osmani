package Network.ServerClientPacket;

import Network.ServerMessageExecutor;

public class SCPLogInSuccess implements ServerClientPacket{

    private final String message;
    private final String username;

    public SCPLogInSuccess(String message, String username) {
        this.message = message;
        this.username = username;
    }


    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.loginSuccess(message, username);
    }
}
