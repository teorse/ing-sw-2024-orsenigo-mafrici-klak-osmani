package Network.ServerClientPacket;

import Client.Model.Records.*;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.Map;

public class SCPGameStarted implements ServerClientPacket{
    private final byte [] playersData;
    private final byte [] secretData;
    private final byte [] tableData;
    private final byte [] gameData;

    public SCPGameStarted(Map<PlayerRecord, CardMapRecord> players, PlayerSecretInfoRecord secret, TableRecord table, GameRecord game){
        byte[] playersDataTemp = null;
        byte[] secretDataTemp = null;
        byte[] tableDataTemp = null;
        byte[] gameDataTemp = null;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(players);
            oos.flush();
            playersDataTemp = bos.toByteArray();

            oos.writeObject(secret);
            oos.flush();
            secretDataTemp = bos.toByteArray();

            oos.writeObject(table);
            oos.flush();
            tableDataTemp = bos.toByteArray();

            oos.writeObject(game);
            oos.flush();
            gameDataTemp = bos.toByteArray();

            oos.close();
            bos.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        playersData = playersDataTemp;
        secretData = secretDataTemp;
        tableData = tableDataTemp;
        gameData = gameDataTemp;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        Map<PlayerRecord, CardMapRecord> players = null;
        PlayerSecretInfoRecord secret = null;
        TableRecord table = null;
        GameRecord game = null;

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(playersData);
            ObjectInputStream in = new ObjectInputStream(bis);
            players = (Map<PlayerRecord, CardMapRecord>) in.readObject();
            in.close();
            bis.close();

            bis = new ByteArrayInputStream(secretData);
            in = new ObjectInputStream(bis);
            secret = (PlayerSecretInfoRecord) in.readObject();
            in.close();
            bis.close();

            bis = new ByteArrayInputStream(tableData);
            in = new ObjectInputStream(bis);
            table = (TableRecord) in.readObject();
            in.close();
            bis.close();

            bis = new ByteArrayInputStream(gameData);
            in = new ObjectInputStream(bis);
            game = (GameRecord) in.readObject();
            in.close();
            bis.close();
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        clientController.gameStarted(players, secret, table, game);
    }
}
