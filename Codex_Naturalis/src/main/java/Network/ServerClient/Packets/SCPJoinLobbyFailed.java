package Network.ServerClient.Packets;

import Network.ServerClient.ServerMessageExecutor;

public class SCPJoinLobbyFailed implements ServerClientPacket{
    private final ErrorsDictionary errorCause;

    public SCPJoinLobbyFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }


    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.joinLobbyFailed(errorCause);
    }
}
