package it.polimi.ingsw.Server.Model.Lobby;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Server.Interfaces.LayerUser;
import it.polimi.ingsw.Server.Model.Server.ServerUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a user within a lobby.
 * It encapsulates information about the user's server-side identity, role within the lobby,
 * connection status, and assigned lobby color.
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

    /**
     * Sets the lobby color of the user.
     *
     * @param color The lobby color to set for the user.
     */
    public void setColor(LobbyUserColors color){
        this.color = color;
    }

    /**
     * Sets the role of the user to administrator.
     * Only applicable within the context of the lobby.
     */
    public void setAdmin(){
        role = LobbyRoles.ADMIN;
    }





    //GETTERS
    /**
     * Retrieves the connection status of the user within the lobby.
     *
     * @return The connection status of the user.
     */
    public LobbyUserConnectionStates getConnectionStatus(){
        return this.connectionStatus;
    }

    /**
     * Retrieves the role of the user within the lobby.
     *
     * @return The role of the user.
     */
    public LobbyRoles getLobbyRole(){
        return this.role;
    }

    /**
     * Retrieves the username of the user associated with this lobby user.
     *
     * @return The username of the user.
     */
    public String getUsername(){
        return serverUser.getUsername();
    }

    /**
     * Retrieves the server user object associated with this lobby user.
     *
     * @return The server user object.
     */
    public ServerUser getServerUser(){
        return this.serverUser;
    }

    /**
     * Retrieves the lobby color assigned to this user.
     *
     * @return The lobby color of the user.
     */
    public LobbyUserColors getColor(){
        return color;
    }





    //EQUALS HASH

    /**
     * Indicates whether some other object is "equal to" this one.
     * Compares this {@code LobbyUser} instance with the specified object.
     * Returns {@code true} if the given object is also a {@code LobbyUser} and
     * both instances have the same {@code serverUser}.
     *
     * @param o The object to compare with this {@code LobbyUser}.
     * @return {@code true} if the objects are the same or have the same {@code serverUser}, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LobbyUser lobbyUser = (LobbyUser) o;
        return Objects.equals(serverUser, lobbyUser.serverUser);
    }

    /**
     * Returns a hash code value for the {@code LobbyUser} object.
     * The hash code is based on the {@code serverUser} of the {@code LobbyUser}.
     *
     * @return A hash code value for this {@code LobbyUser} object.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(serverUser);
    }





    //TO RECORD METHODS
    /**
     * Converts the current state of this {@code LobbyUser} into a data transfer object {@code LobbyUserRecord}.
     * The record contains information about the user's username, role, color, and connection status.
     *
     * @return The {@code LobbyUserRecord} representing the current state of this {@code LobbyUser}.
     */
    protected LobbyUserRecord toRecord(){
        return new LobbyUserRecord(getUsername(), role, color, connectionStatus);
    }
}
