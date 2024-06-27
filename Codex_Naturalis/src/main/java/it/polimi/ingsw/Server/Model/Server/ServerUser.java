package it.polimi.ingsw.Server.Model.Server;

import it.polimi.ingsw.Server.Interfaces.LayerUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a user on the server.
 */
public class ServerUser implements Serializable, LayerUser {
    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -591501703051340798L;
    private final String username;
    private String connectionStatus;





    //CONSTRUCTOR
    /**
     * Constructs a new ServerUser with the given username.
     *
     * @param username The username of the user.
     */
    public ServerUser(String username){
        this.username = username;
    }





    //SETTERS
    /**
     * Sets the connection status of the user to online.
     */
    public void setOnline(){
        connectionStatus = "ONLINE";
    }

    /**
     * Sets the connection status of the user to offline.
     */
    public void setOffline(){
        connectionStatus = "OFFLINE";
    }





    //GETTERS
    /**
     * Gets the connection status of the user.
     *
     * @return The connection status of the user.
     */
    public String getConnectionStatus(){
        return this.connectionStatus;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername(){
        return this.username;
    }





    //EQUALS HASH

    /**
     * Indicates whether some other object is "equal to" this one.
     * Compares this {@code ServerUser} instance with the specified object.
     * Returns {@code true} if the given object is also a {@code ServerUser} and
     * both instances have the same {@code username}.
     *
     * @param o The object to compare with this {@code ServerUser}.
     * @return {@code true} if the objects are the same or have the same {@code username}, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUser that = (ServerUser) o;
        return Objects.equals(username, that.username);
    }

    /**
     * Returns a hash code value for the {@code ServerUser} object.
     * The hash code is based on the {@code username} of the {@code ServerUser}.
     *
     * @return A hash code value for this {@code ServerUser} object.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
