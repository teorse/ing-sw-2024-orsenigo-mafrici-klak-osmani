package Network.ServerClientPacket;

import Client.Model.Records.PlayerSecretInfoRecord;
import Network.ServerMessageExecutor;

import java.io.*;

public class SCPUpdateSecret implements ServerClientPacket{
    private final byte[] secretData;

    public SCPUpdateSecret(PlayerSecretInfoRecord secret) {
        byte[] secretDataTemp = null;

        try {
            // Serialize the secret info record
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(secret);
            oos.flush();
            secretDataTemp = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        secretData = secretDataTemp;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        PlayerSecretInfoRecord secret = null;

        try {
            // Deserialize the secret info record
            ByteArrayInputStream bis = new ByteArrayInputStream(secretData);
            ObjectInputStream in = new ObjectInputStream(bis);
            secret = (PlayerSecretInfoRecord) in.readObject();
            in.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        // Pass the deserialized secret info record to the client controller
        clientController.updateSecret(secret);
    }
}
