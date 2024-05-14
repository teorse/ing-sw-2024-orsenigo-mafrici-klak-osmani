package Network.ServerClient.Packets;

import Network.ServerClient.ServerMessageExecutor;

public class SCPStartLobbyFailed implements ServerClientPacket{

    private final ErrorsDictionary errorCause;

    public SCPStartLobbyFailed(ErrorsDictionary errorCause) {
        this.errorCause = errorCause;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.startLobbyFailed(errorCause);
    }
}
