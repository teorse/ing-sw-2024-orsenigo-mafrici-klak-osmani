package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
