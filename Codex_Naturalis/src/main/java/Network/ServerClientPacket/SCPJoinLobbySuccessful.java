package Network.ServerClientPacket;

import Client.Model.Records.LobbyRecord;
import Client.Model.Records.LobbyUserRecord;
import Network.ServerMessageExecutor;

import java.io.*;
import java.util.List;

public class SCPJoinLobbySuccessful implements ServerClientPacket {
    private final byte [] lobbyData;
    private final byte [] lobbyUsersData;
    private final String notification;

    public SCPJoinLobbySuccessful(LobbyRecord lobbyRecord, List<LobbyUserRecord> lobbyUsers, String notification){
        byte[] lobbyUsersData1;
        byte[] lobbyData1;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(lobbyRecord);
            oos.flush();
            lobbyData1 = bos.toByteArray();

            oos.writeObject(lobbyUsers);
            oos.flush();
            lobbyUsersData1 = bos.toByteArray();
        }
        catch (IOException e){
            lobbyData1 = null;
            lobbyUsersData1 = null;

            System.out.println(e);
        }
        lobbyData = lobbyData1;
        lobbyUsersData = lobbyUsersData1;
        this.notification = notification;
    }

    @Override
    public void execute(ServerMessageExecutor clientController) {
        LobbyRecord lobbyRecord = null;
        List<LobbyUserRecord> lobbyUsers = null;

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(lobbyData);
            ObjectInputStream in = new ObjectInputStream(bis);
            lobbyRecord = (LobbyRecord) in.readObject();
            in.close();
            bis.close();

            bis = new ByteArrayInputStream(lobbyUsersData);
            in = new ObjectInputStream(bis);
            lobbyUsers = (List<LobbyUserRecord>) in.readObject();
            in.close();
            bis.close();
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println(e);
        }

        clientController.joinLobbySuccessful(lobbyRecord, lobbyUsers, notification);
    }
}
