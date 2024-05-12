package Network.ServerClientPacket.Demo;

import Client.Model.Records.LobbyPreviewRecord;
import Network.ServerClientPacket.ServerClientPacket;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SCPUpdateLobbyPreviewsDemo implements ServerClientPacket, Serializable {

    byte [] data;

    public SCPUpdateLobbyPreviewsDemo(Set<LobbyPreviewRecord> lobbyPreviewSet){
        try {
            //Storing the array as byte stream to solve problems on the client side
            //If storing the array normally, on client side the values would not update after de-serialization of message.
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(lobbyPreviewSet);
            oos.flush();
            data = bos.toByteArray();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        Set<LobbyPreviewRecord> lobbyPreviewSet = new HashSet<>();
        try {
            //List is deserialized from the byte stream
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            lobbyPreviewSet = (Set<LobbyPreviewRecord>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println(e);
        }

        String lobbyPreview = "\nLobby preview:\n";

        for(LobbyPreviewRecord preview : lobbyPreviewSet){
            lobbyPreview = lobbyPreview + "Lobby name: "+preview.lobbyName()+"\n" +
                    "Participants: "+preview.currentUsers()+"/"+preview.maxUsers()+"\n"+
                    "Game started: "+preview.gameStarted()+"\n\n";

        }

        System.out.println(lobbyPreview);

    }
}
