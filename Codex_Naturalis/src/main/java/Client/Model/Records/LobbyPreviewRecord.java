package Client.Model.Records;

import Server.Model.LobbyPreviewObserverRelay;

import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;

/**
 * This class represents a preview of a lobby, providing an overview of its statistics without querying the lobby directly.
 */
public class LobbyPreviewRecord implements Serializable {
    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -2700460455118777031L;
    private final PropertyChangeSupport pcs;
    private final String lobbyName;
    private int users;
    private boolean gameStarted;





    //CONSTRUCTOR
    /**
     * Constructs a new LobbyPreview object with the given lobby name.
     *
     * @param lobbyName The name of the lobby.
     */
    public LobbyPreviewRecord(String lobbyName){
        pcs = new PropertyChangeSupport(this);
        this.lobbyName = lobbyName;
        users = 0;
        gameStarted = false;
    }





    //GETTERS
    public String getLobbyName() {
        return lobbyName;
    }

    public int getUsers() {
        return users;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }






    //SETTERS
    public void setUsers(int users) {
        this.users = users;
        pcs.firePropertyChange("previewUpdated", null, this);
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        pcs.firePropertyChange("previewUpdated", null, this);
    }





    //OBSERVER METHODS
    public void addObserver(LobbyPreviewObserverRelay observer){
        pcs.addPropertyChangeListener(observer);
    }

    public void removeObserver(LobbyPreviewObserverRelay observer){
        pcs.removePropertyChangeListener(observer);
    }
}
