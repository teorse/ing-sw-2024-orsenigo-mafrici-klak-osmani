package Server.Model;

import Network.ServerClientPacket.Demo.SCPUpdateLobbyPreviewsDemo;
import Network.ServerClientPacket.ServerClientPacket;
import Client.Model.Records.LobbyPreviewRecord;
import Server.Network.ClientHandler.ClientHandler;

import java.util.*;

/**
 * This class acts as an observer relay for lobby preview updates, notifying all subscribed observers
 * when a change occurs in any of the observed lobby previews.
 */
public class LobbyPreviewObserverRelay {
    //ATTRIBUTES
    private final List<ClientHandler> observers;
    private final Map<String, LobbyPreviewRecord> lobbyPreviewMap;





    //CONSTRUCTOR
    /**
     * Constructs a new LobbyPreviewObserverRelay with the lobby preview map provided by the server.
     *
     * @param lobbyPreviewMap The map containing lobby previews to observe.
     */
    public LobbyPreviewObserverRelay(){
        observers = new ArrayList<>();
        lobbyPreviewMap = new HashMap<>();
    }





    //OBSERVER METHODS
    public void updateLobbyPreview(LobbyPreviewRecord preview){
        String lobbyName = preview.lobbyName();
        lobbyPreviewMap.put(lobbyName, preview);

        ServerClientPacket packet = new SCPUpdateLobbyPreviewsDemo(Collections.unmodifiableMap(lobbyPreviewMap));
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
        ServerClientPacket packet = new SCPUpdateLobbyPreviewsDemo(Collections.unmodifiableMap(lobbyPreviewMap));
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
