package Server.Model;

import Network.ServerClientPacket.SCPUpdateLobbyPreviews;
import Network.ServerClientPacket.ServerClientPacket;
import Client.Model.Records.LobbyPreviewRecord;
import Server.Network.ClientHandler.ClientHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class acts as an observer relay for lobby preview updates, notifying all subscribed observers
 * when a change occurs in any of the observed lobby previews.
 */
public class LobbyPreviewObserverRelay implements PropertyChangeListener {
    //ATTRIBUTES
    private final List<ClientHandler> observers;
    private final Map<String, LobbyPreviewRecord> lobbyPreviewMap;





    //CONSTRUCTOR
    /**
     * Constructs a new LobbyPreviewObserverRelay with the lobby preview map provided by the server.
     *
     * @param lobbyPreviewMap The map containing lobby previews to observe.
     */
    public LobbyPreviewObserverRelay(Map<String, LobbyPreviewRecord> lobbyPreviewMap){
        observers = new ArrayList<>();
        this.lobbyPreviewMap = lobbyPreviewMap;
    }





    //OBSERVER METHODS
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        ServerClientPacket packet = new SCPUpdateLobbyPreviews(Collections.unmodifiableMap(lobbyPreviewMap));
        for(ClientHandler observer : observers)
            observer.sendPacket(packet);
    }

    /**
     * Adds an observer to the relay.
     *
     * @param observer The observer to add.
     */
    public void addObserver(ClientHandler observer){
        observers.add(observer);
        ServerClientPacket packet = new SCPUpdateLobbyPreviews(Collections.unmodifiableMap(lobbyPreviewMap));
        observer.sendPacket(packet);
    }

    /**
     * Removes an observer from the relay.
     *
     * @param observer The observer to remove.
     */
    public void removeObserver(ClientHandler observer){
        observers.remove(observer);
    }
}
