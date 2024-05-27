package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPStartLobbyFailed implements ServerClientPacket{

    private final ErrorsDictionary errorCause;

    public SCPStartLobbyFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.startLobbyFailed(errorCause);
    }
}
