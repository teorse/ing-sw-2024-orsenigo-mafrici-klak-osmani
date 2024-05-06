package Network.ServerClientPacket;

import Client.Model.Records.LobbyPreviewRecord;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.List;

public class SCPUpdateLobbyPreviews implements ServerClientPacket{
    byte [] data;

    public SCPUpdateLobbyPreviews(List<LobbyPreviewRecord> lobbyPreviews){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(lobbyPreviews);
            oos.flush();
            data = bos.toByteArray();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        List<LobbyPreviewRecord> lobbyPreviews = null;
        try {
            //List is deserialized from the byte stream
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            lobbyPreviews = (List<LobbyPreviewRecord>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println(e);
        }

        clientController.updateLobbyPreviews(lobbyPreviews);
    }
}
