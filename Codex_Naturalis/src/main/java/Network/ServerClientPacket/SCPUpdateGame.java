package Network.ServerClientPacket;

import Client.Model.Records.GameRecord;
import Network.ServerMessageExecutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SCPUpdateGame implements ServerClientPacket {
    private final byte[] gameData;

    public SCPUpdateGame(GameRecord game) {
        byte[] gameDataTemp = null;

        try {
            // Serialize the game record
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(game);
            oos.flush();
            gameDataTemp = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        gameData = gameDataTemp;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        GameRecord game = null;

        try {
            // Deserialize the game record
            ByteArrayInputStream bis = new ByteArrayInputStream(gameData);
            ObjectInputStream in = new ObjectInputStream(bis);
            game = (GameRecord) in.readObject();
            in.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        // Pass the deserialized game record to the client controller
        clientController.updateGame(game);
    }
}
