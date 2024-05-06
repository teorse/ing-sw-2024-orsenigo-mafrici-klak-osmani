package Network.ServerClientPacket;

import Client.Model.Records.LobbyPreviewRecord;
import Client.Model.Records.LobbyUserRecord;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.List;

public class SCPUpdateLobbyUsers implements ServerClientPacket{
    byte [] data;

    public SCPUpdateLobbyUsers(List<LobbyUserRecord> lobbyUsers){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(lobbyUsers);
            oos.flush();
            data = bos.toByteArray();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        List<LobbyUserRecord> lobbyUsers = null;
        try {
            //List is deserialized from the byte stream
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            lobbyUsers = (List<LobbyUserRecord>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println(e);
        }

        clientController.updateLobbyUsers(lobbyUsers);
    }
}
