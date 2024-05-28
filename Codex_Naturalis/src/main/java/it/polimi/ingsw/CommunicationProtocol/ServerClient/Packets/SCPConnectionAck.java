package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPConnectionAck implements ServerClientPacket{

    private final String message;

    public SCPConnectionAck(String message) {
        this.message = message;
    }


    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        clientController.connectionAck(message);
    }
}
