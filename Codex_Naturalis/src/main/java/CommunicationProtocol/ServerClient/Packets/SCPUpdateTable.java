package CommunicationProtocol.ServerClient.Packets;

import CommunicationProtocol.ServerClient.DataTransferObjects.TableRecord;
import CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

public class SCPUpdateTable implements ServerClientPacket {
    private final TableRecord table;

    public SCPUpdateTable(TableRecord table) {
        this.table = table;
    }

    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized table record to the client controller
        clientController.updateTable(table);
    }
}
