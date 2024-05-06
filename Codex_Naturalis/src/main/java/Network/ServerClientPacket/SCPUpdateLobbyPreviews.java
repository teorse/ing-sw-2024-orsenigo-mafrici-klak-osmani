package Network.ServerClientPacket;

import Client.Controller.ClientController;
import Client.Model.Records.LobbyPreviewRecord;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SCPUpdateLobbyPreviews implements ServerClientPacket, Serializable {

    byte [] data;

    public SCPUpdateLobbyPreviews(Map<String, LobbyPreviewRecord> lobbyPreviewMap){
        try {
            //Storing the array as byte stream to solve problems on the client side
            //If storing the array normally, on client side the values would not update after de-serialization of message.
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(lobbyPreviewMap);
            oos.flush();
            data = bos.toByteArray();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    @Override
    public void execute(ClientController clientController) {
        Map<String, LobbyPreviewRecord> lobbyPreviewMap = new HashMap<>();
        try {
            //List is deserialized from the byte stream
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            lobbyPreviewMap = (Map<String, LobbyPreviewRecord>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println(e);
        }

        String lobbyPreview = "\nLobby preview:\n";

        for(Map.Entry<String, LobbyPreviewRecord> entry : lobbyPreviewMap.entrySet()){
            LobbyPreviewRecord preview = entry.getValue();
            lobbyPreview = lobbyPreview + "Lobby name: "+preview.getLobbyName()+"\n" +
                    "Participants: "+preview.getUsers()+"/4\n"+
                    "Game started: "+preview.isGameStarted()+"\n\n";

        }

        System.out.println(lobbyPreview);

    }
}
