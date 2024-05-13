package Network.ServerClient.Demo;

import Network.ServerClient.Packets.ServerClientPacket;
import Network.ServerMessageExecutor;
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

    /**
     * Constructs an SCMUpdateLobbyUsers object with the specified list of lobby users.
     *
     * @param lobbyUsers The list of lobby users to be updated on the client side.
     */
    public SCPUpdateLobbyUsers(List<LobbyUser> lobbyUsers){
        this.lobbyUsers = lobbyUsers;
    }

    /**
     * Executes the message on the client side by updating the list of lobby users.
     *
     * @param clientController The ClientController object on the client side.
     */
    @Override
    public void execute(ServerMessageExecutor clientController) {

        StringBuilder usersStatus = new StringBuilder("USERS IN LOBBY:");
        for(LobbyUser lobbyUser : lobbyUsers){
            usersStatus.append("\n").append(lobbyUser.getColor()).append(" ").append(lobbyUser.getUsername()).append(" : ").append(lobbyUser.getConnectionStatus());
        }
        System.out.println(usersStatus);
    }
}
