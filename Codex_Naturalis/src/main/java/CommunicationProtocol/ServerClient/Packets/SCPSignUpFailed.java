package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPSignUpFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    public SCPSignUpFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.signUpFailed(errorCause);
    }
}
