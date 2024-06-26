package it.polimi.ingsw.Server.Model.Lobby;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ServerClientPacket;
import it.polimi.ingsw.Server.Network.ClientHandler.ClientHandler;

import java.util.HashMap;
import java.util.Map;

public class ObserverRelay {
    private final Map<String, ClientHandler> observers;

    /**
     * Constructs a new ObserverRelay with an empty map of observers.
     */
    public ObserverRelay() {
        observers = new HashMap<>();
    }

    /**
     * Subscribes an observer (ClientHandler) to the relay.
     *
     * @param username   The username of the observer.
     * @param connection The ClientHandler instance representing the observer.
     */
    public void subscribe(String username, ClientHandler connection) {
        observers.put(username, connection);
    }

    /**
     * Unsubscribes an observer (ClientHandler) from the relay.
     *
     * @param username The username of the observer to unsubscribe.
     */
    public void unsubscribe(String username) {
        observers.remove(username);
    }

    /**
     * Sends a packet to all subscribed observers.
     *
     * @param packet The ServerClientPacket to send to all observers.
     */
    public void update(ServerClientPacket packet) {
        for (ClientHandler observer : observers.values()) {
            observer.sendPacket(packet);
        }
    }

    /**
     * Sends a packet to a specific observer.
     *
     * @param username The username of the observer to send the packet to.
     * @param packet   The ServerClientPacket to send.
     */
    public void update(String username, ServerClientPacket packet) {
        ClientHandler observer = observers.get(username);
        if (observer != null) {
            observer.sendPacket(packet);
        }
    }
}
