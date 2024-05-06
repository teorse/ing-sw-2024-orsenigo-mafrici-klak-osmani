package Network.ServerClientPacket;

import Network.ServerMessageExecutor;

public class SCPLogInFailed implements ServerClientPacket{
    private final String message;

    public SCPLogInFailed(String message) {
        this.message = message;
    }


    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.loginFailed(message);
    }
}
