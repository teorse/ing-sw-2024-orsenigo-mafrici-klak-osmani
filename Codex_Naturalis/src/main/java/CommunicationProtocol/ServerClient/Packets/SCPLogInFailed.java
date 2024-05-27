package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPLogInFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    public SCPLogInFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }


    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.loginFailed(errorCause);
    }
}
