package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
