package Network.ServerClientPacket;

import Client.Model.Records.TableRecord;
import Network.ServerMessageExecutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SCPUpdateTable implements ServerClientPacket {
    private final byte[] tableData;

    public SCPUpdateTable(TableRecord table) {
        byte[] tableDataTemp = null;

        try {
            // Serialize the table record
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(table);
            oos.flush();
            tableDataTemp = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        tableData = tableDataTemp;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        TableRecord table = null;

        try {
            // Deserialize the table record
            ByteArrayInputStream bis = new ByteArrayInputStream(tableData);
            ObjectInputStream in = new ObjectInputStream(bis);
            table = (TableRecord) in.readObject();
            in.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        // Pass the deserialized table record to the client controller
        clientController.updateTable(table);
    }
}
