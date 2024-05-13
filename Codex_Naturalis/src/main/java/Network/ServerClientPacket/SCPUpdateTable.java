package Network.ServerClientPacket;

import Client.Model.Records.TableRecord;
import Network.ServerMessageExecutor;

public class SCPUpdateTable implements ServerClientPacket {
    private final TableRecord table;

    public SCPUpdateTable(TableRecord table) {
        this.table = table;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        // Pass the deserialized table record to the client controller
        clientController.updateTable(table);
    }
}
