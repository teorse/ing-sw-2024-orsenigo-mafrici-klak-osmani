package Network.ServerClientPacket.Demo;

import Client.Model.Records.LobbyPreviewRecord;
import Network.ServerClientPacket.ServerClientPacket;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SCPUpdateLobbyPreviewsDemo implements ServerClientPacket, Serializable {

    byte [] data;

    public SCPUpdateLobbyPreviewsDemo(Map<String, LobbyPreviewRecord> lobbyPreviewMap){
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
    public void execute(ServerMessageExecutor clientController) {
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
            lobbyPreview = lobbyPreview + "Lobby name: "+preview.lobbyName()+"\n" +
                    "Participants: "+preview.currentUsers()+"/"+preview.maxUsers()+"\n"+
                    "Game started: "+preview.gameStarted()+"\n\n";

        }

        System.out.println(lobbyPreview);

    }
}
