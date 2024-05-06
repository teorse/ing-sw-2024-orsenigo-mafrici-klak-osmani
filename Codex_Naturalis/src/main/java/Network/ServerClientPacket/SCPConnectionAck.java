package Network.ServerClientPacket;

import Network.ServerMessageExecutor;

public class SCPConnectionAck implements ServerClientPacket{

    private final String message;

    public SCPConnectionAck(String message) {
        this.message = message;
    }


    @Override
    public void execute(ServerMessageExecutor clientController) {
        clientController.connectionAck(message);
    }
}
