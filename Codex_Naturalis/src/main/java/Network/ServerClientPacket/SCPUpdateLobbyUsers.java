package Network.ServerClientPacket;

import Client.Controller.ClientController;
import Server.Model.Lobby.LobbyUser;

import java.io.*;
import java.util.List;

/**
 * The SCMUpdateLobbyUsers class implements the ServerClientMessage interface.<br>
 * It represents a message sent from the server to update the list of users in the lobby on the client side.
 */
public class SCPUpdateLobbyUsers implements ServerClientPacket, Serializable {
    @Serial
    private static final long serialVersionUID = -887361246827442009L;
    private List<LobbyUser> lobbyUsers;
    byte [] data;

    /**
     * Constructs an SCMUpdateLobbyUsers object with the specified list of lobby users.
     *
     * @param lobbyUsers The list of lobby users to be updated on the client side.
     */
    public SCPUpdateLobbyUsers(List<LobbyUser> lobbyUsers){
        try {
            //Storing the array as byte stream to solve problems on the client side
            //If storing the array normally, on client side the values would not update after de-serialization of message.
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

    /**
     * Executes the message on the client side by updating the list of lobby users.
     *
     * @param clientController The ClientController object on the client side.
     */
    @Override
    public void execute(ClientController clientController) {

        try {
            //List is deserialized from the byte stream
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            lobbyUsers = (List<LobbyUser>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println(e);
        }

        StringBuilder usersStatus = new StringBuilder("USERS IN LOBBY:");
        for(LobbyUser lobbyUser : lobbyUsers){
            usersStatus.append("\n").append(lobbyUser.getUsername()).append(" : ").append(lobbyUser.getConnectionStatus());
        }
        System.out.println(usersStatus);
    }
}
