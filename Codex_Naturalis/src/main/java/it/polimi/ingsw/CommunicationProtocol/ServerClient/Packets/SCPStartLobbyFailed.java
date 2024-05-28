package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

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
