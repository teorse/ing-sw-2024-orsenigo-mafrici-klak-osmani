package it.polimi.ingsw.Server.Model.Server;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateLobbyPreviews;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ServerClientPacket;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;

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
        lobbyPreviewSet.remove(preview);
        lobbyPreviewSet.add(preview);
        broadCastPreviews();
    }

    public void removeLobbyPreview(LobbyPreviewRecord preview){
        lobbyPreviewSet.remove(preview);
        broadCastPreviews();
    }

    private void broadCastPreviews(){
        ServerClientPacket packet = new SCPUpdateLobbyPreviews(lobbyPreviewSet.stream().toList());

        for(ClientHandler observer : observers.values()) {
            System.out.println("Sending Lobby preview update to users");
            observer.sendPacket(packet);
        }
    }

    /**
     * Adds an observer to the relay.
     *
     * @param observer The observer to add.
     */
    public void addObserver(String username, ClientHandler observer){
        observers.put(username, observer);
        ServerClientPacket packet = new SCPUpdateLobbyPreviews(lobbyPreviewSet.stream().toList());
        System.out.println("Added new observer and Sending Lobby preview update to them");
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