package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
