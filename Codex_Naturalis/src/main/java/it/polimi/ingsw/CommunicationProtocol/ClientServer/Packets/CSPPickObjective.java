package it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets;

import it.polimi.ingsw.CommunicationProtocol.ClientServer.ClientServerMessageExecutor;

public class CSPPickObjective implements ClientServerPacket{
    private final int objectiveIndex;

    public CSPPickObjective(int objectiveIndex) {
        this.objectiveIndex = objectiveIndex;
    }

    @Override
    public void execute(ClientServerMessageExecutor executor) {
        executor.pickObjective(objectiveIndex);
    }
}
