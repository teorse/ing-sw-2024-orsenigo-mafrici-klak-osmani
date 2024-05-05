package Server.Model.Lobby;

import Server.Interfaces.LayerUser;
import Server.Model.ServerUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a user within a lobby.
 */
public class LobbyUser implements Serializable, LayerUser {
    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -1039118189828128709L;
    private final ServerUser serverUser;
    private final LobbyRoles role;
    private LobbyUserConnectionStates connectionStatus;





    //CONSTRUCTOR
    /**
     * Constructs a new LobbyUser object with the given server user and lobby role.
     *
     * @param serverUser The server user associated with this lobby user.
     * @param role  The role of the user within the lobby.
     */
    public LobbyUser(ServerUser serverUser, LobbyRoles role){
        this.serverUser = serverUser;
        this.role = role;
        connectionStatus = LobbyUserConnectionStates.ONLINE;
    }





    //SETTERS
    /**
     * Sets the connection status of the user to online.
     */
    public void setOnline(){
        connectionStatus = LobbyUserConnectionStates.OFFLINE;
    }

    /**
     * Sets the connection status of the user to offline.
     */
    public void setOffline(){
        connectionStatus = LobbyUserConnectionStates.OFFLINE;
    }





    //GETTERS
    public LobbyUserConnectionStates getConnectionStatus(){
        return this.connectionStatus;
    }

    public LobbyRoles getLobbyRole(){
        return this.role;
    }

    public String getUsername(){
        return serverUser.getUsername();
    }

    public ServerUser getServerUser(){
        return this.serverUser;
    }





    //EQUALS HASH

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LobbyUser lobbyUser = (LobbyUser) o;
        return Objects.equals(serverUser, lobbyUser.serverUser);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serverUser);
    }
}
