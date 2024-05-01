package Server.Model.Lobby;

import Network.ServerClientPacket.SCPUpdateLobbyUsers;
import Network.ServerClientPacket.ServerClientPacket;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LobbyListener implements PropertyChangeListener {
    private final Lobby lobby;

    public LobbyListener(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("LobbyUsersChange")){
            ServerClientPacket message = new SCPUpdateLobbyUsers(lobby.getUsers());
            lobby.broadcastPacket(message);
        }
    }
}
