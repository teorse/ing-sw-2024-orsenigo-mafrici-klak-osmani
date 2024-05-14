package Network.ServerClient.Packets;

import Network.ServerClient.ServerMessageExecutor;

public class SCPLogInFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    public SCPLogInFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }


    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.loginFailed(errorCause);
    }
}
