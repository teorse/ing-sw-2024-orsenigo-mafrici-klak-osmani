package Network.ServerClient.Packets;

import Network.ServerClient.ServerMessageExecutor;

public class SCPSignUpFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    public SCPSignUpFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.signUpFailed(errorCause);
    }
}
