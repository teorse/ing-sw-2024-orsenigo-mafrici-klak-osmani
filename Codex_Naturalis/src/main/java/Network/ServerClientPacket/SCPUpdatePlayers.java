package Network.ServerClientPacket;

import Client.Model.Records.CardMapRecord;
import Client.Model.Records.PlayerRecord;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.Map;

public class SCPUpdatePlayers implements ServerClientPacket{
    private final byte[] playersData;

    public SCPUpdatePlayers(Map<PlayerRecord, CardMapRecord> players) {
        byte[] playersDataTemp = null;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(players);
            oos.flush();
            playersDataTemp = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        playersData = playersDataTemp;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        Map<PlayerRecord, CardMapRecord> players = null;

        try {
            // Deserialize the players map
            ByteArrayInputStream bis = new ByteArrayInputStream(playersData);
            ObjectInputStream in = new ObjectInputStream(bis);
            players = (Map<PlayerRecord, CardMapRecord>) in.readObject();
            in.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        // Pass the deserialized players map to the client controller
        clientController.updatePlayers(players);
    }
}
