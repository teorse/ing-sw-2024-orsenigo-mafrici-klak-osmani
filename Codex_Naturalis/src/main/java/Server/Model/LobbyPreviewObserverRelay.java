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
    private final Map<String, ClientHandler> observers;
    private final Set<LobbyPreviewRecord> lobbyPreviewSet;





    //CONSTRUCTOR
    /**
     * Constructs a new LobbyPreviewObserverRelay with the lobby preview map provided by the server.
     */
    public LobbyPreviewObserverRelay(){
        observers = new HashMap<>();
        lobbyPreviewSet = new HashSet<>();
    }





    //OBSERVER METHODS
    public void updateLobbyPreview(LobbyPreviewRecord preview){
        lobbyPreviewSet.add(preview);

        ServerClientPacket packet = new SCPUpdateLobbyPreviewsDemo(lobbyPreviewSet);

        for(ClientHandler observer : observers.values())
            observer.sendPacket(packet);
    }

    /**
     * Adds an observer to the relay.
     *
     * @param observer The observer to add.
     */
    public void addObserver(String username, ClientHandler observer){
        observers.put(username, observer);
        ServerClientPacket packet = new SCPUpdateLobbyPreviewsDemo(lobbyPreviewSet);
        observer.sendPacket(packet);
    }

    /**
     * Removes an observer from the relay.
     *
     * @param username The observer to remove.
     */
    public void removeObserver(String username){
        observers.remove(username);
    }
}
