package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.TableRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.ServerClientMessageExecutor;

/**
 * Packet sent from the server to the client to update the table information.
 * Contains a {@code TableRecord} object representing the updated table information.
 * Upon receiving this packet, the client invokes {@code updateTable} method
 * on the {@code ServerClientMessageExecutor}.
 */
public class SCPUpdateTable implements ServerClientPacket {
    private final TableRecord table;

    /**
     * Constructs an {@code SCPUpdateTable} instance with the specified table record.
     *
     * @param table The table record containing the updated table information.
     */
    public SCPUpdateTable(TableRecord table) {
        this.table = table;
    }

    /**
     * Executes the packet's operation on the client side, specifically invoking {@code updateTable} method.
     *
     * @param clientController The controller responsible for handling messages on the client side.
     *                         This method updates the table information with the data received from the server.
     */
    @Override
    public void execute(ServerClientMessageExecutor clientController) {
        // Pass the deserialized table record to the client controller
        clientController.updateTable(table);
    }
}
