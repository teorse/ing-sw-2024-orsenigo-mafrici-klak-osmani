package Server.Model.Lobby.GameDemo;

import Network.ServerClientPacket.SCPPrintPlaceholder;
import Network.ServerClientPacket.ServerClientPacket;
import Server.Model.Lobby.Lobby;
import Server.Model.Lobby.LobbyUser;
import Server.Network.ClientHandler.ClientHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlayerDataListener implements PropertyChangeListener {
    private Player observedPlayer;
    private Lobby lobby;

    public PlayerDataListener(Lobby lobby){
        this.lobby = lobby;
    }

    public void setObservedPlayer(Player player){
        this.observedPlayer = player;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ClientHandler ch;
        ServerClientPacket message;

        if("personalData".equals(evt.getPropertyName())) {
            LobbyUser lobbyUser = observedPlayer.getUser();
            message = new SCPPrintPlaceholder("Personal data: " + evt.getNewValue());
            lobby.sendPacket(lobbyUser, message);
        }

        if("publicData".equals(evt.getPropertyName())){
            message = new SCPPrintPlaceholder("Public data from player "+observedPlayer+": "+evt.getNewValue());
            lobby.broadcastPacket(message);
        }
    }
}
