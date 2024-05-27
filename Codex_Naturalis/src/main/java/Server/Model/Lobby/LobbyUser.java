package Server.Model.Lobby;

import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import Server.Interfaces.LayerUser;
import Server.Model.Server.ServerUser;

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
    private LobbyRoles role;
    private LobbyUserColors color;
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
        connectionStatus = LobbyUserConnectionStates.ONLINE;
    }

    /**
     * Sets the connection status of the user to offline.
     */
    public void setOffline(){
        connectionStatus = LobbyUserConnectionStates.OFFLINE;
    }

    public void setColor(LobbyUserColors color){
        this.color = color;
    }

    public void setAdmin(){
        role = LobbyRoles.ADMIN;
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

    public LobbyUserColors getColor(){
        return color;
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





    //TO RECORD METHODS
    protected LobbyUserRecord toRecord(){
        return new LobbyUserRecord(getUsername(), role, color, connectionStatus);
    }
}
