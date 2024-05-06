package Network.ServerClientPacket;

import Client.Model.Records.CardMapRecord;
import Network.ServerMessageExecutor;

import java.io.*;

public class SCPUpdateCardMap implements ServerClientPacket{
    private final String ownerUsername;
    private final byte[] cardMapData;

    public SCPUpdateCardMap(String ownerUsername, CardMapRecord cardMap) {
        this.ownerUsername = ownerUsername;

        byte[] cardMapDataTemp = null;

        try {
            // Serialize the card map
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(cardMap);
            oos.flush();
            cardMapDataTemp = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        cardMapData = cardMapDataTemp;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        CardMapRecord cardMap = null;

        try {
            // Deserialize the card map
            ByteArrayInputStream bis = new ByteArrayInputStream(cardMapData);
            ObjectInputStream in = new ObjectInputStream(bis);
            cardMap = (CardMapRecord) in.readObject();
            in.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        // Pass the deserialized card map and owner username to the client controller
        clientController.updateCardMap(ownerUsername, cardMap);
    }
}
