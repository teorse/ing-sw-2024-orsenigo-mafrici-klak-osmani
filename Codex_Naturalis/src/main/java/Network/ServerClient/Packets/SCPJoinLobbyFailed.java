package Network.ServerClient.Packets;

import Network.ServerMessageExecutor;

public class SCPJoinLobbyFailed implements ServerClientPacket{
    private final String message;

    public SCPJoinLobbyFailed(String message) {
        this.message = message;
    }


    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.joinLobbyFailed(message);
    }
}
