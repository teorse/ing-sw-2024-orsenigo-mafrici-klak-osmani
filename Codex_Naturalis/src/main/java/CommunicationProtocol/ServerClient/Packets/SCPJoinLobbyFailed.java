package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPJoinLobbyFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    public SCPJoinLobbyFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }


    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.joinLobbyFailed(errorCause);
    }
}
