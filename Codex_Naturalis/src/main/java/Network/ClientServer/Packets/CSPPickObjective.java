package Network.ClientServer.Packets;

import Server.Controller.InputHandler.ServerInputExecutor;

public class CSPPickObjective implements ClientServerPacket{
    private final int objectiveIndex;

    public CSPPickObjective(int objectiveIndex) {
        this.objectiveIndex = objectiveIndex;
    }

    @Override
    public void execute(ServerInputExecutor executor) {
        executor.pickObjective(objectiveIndex);
    }
}
