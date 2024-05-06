package Network.ServerClientPacket;

import Client.Model.Records.PlayerRecord;
import Network.ServerMessageExecutor;

import java.io.*;

public class SCPUpdateSpecificPlayer implements ServerClientPacket{
    private final byte[] playerData;

    public SCPUpdateSpecificPlayer(PlayerRecord player) {
        byte[] playerDataTemp = null;

        try {
            // Serialize the player record
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(player);
            oos.flush();
            playerDataTemp = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        playerData = playerDataTemp;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        PlayerRecord player = null;

        try {
            // Deserialize the player record
            ByteArrayInputStream bis = new ByteArrayInputStream(playerData);
            ObjectInputStream in = new ObjectInputStream(bis);
            player = (PlayerRecord) in.readObject();
            in.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        // Pass the deserialized player record to the client controller
        clientController.updateSpecificPlayer(player);
    }
}
