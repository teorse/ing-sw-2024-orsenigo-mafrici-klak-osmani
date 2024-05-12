package Server.Model.Lobby;

import Network.ServerClientPacket.ServerClientPacket;
import Server.Network.ClientHandler.ClientHandler;

import java.util.HashMap;
import java.util.Map;

public class ObserverRelay {
    private final Map<String, ClientHandler> observers;


    public ObserverRelay(){
        observers = new HashMap<>();
    }

    public void subscribe(String username, ClientHandler connection){
        observers.put(username, connection);
    }
    public void unsubscribe(String username){
        observers.remove(username);
    }

    public void update(ServerClientPacket packet){
        for(ClientHandler observer : observers.values())
            observer.sendPacket(packet);
    }
    public void update(String username, ServerClientPacket packet){
        ClientHandler observer = observers.get(username);
        observer.sendPacket(packet);
    }
}
