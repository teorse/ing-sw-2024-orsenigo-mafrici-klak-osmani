package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;

import java.util.ArrayList;
import java.util.List;

public class Players extends Observable{
    //SINGLETON PATTERN
    private static Players INSTANCE;
    private Players(){
        players = new ArrayList<>();
    }
    public synchronized static Players getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Players();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private final List<PlayerRecord> players;





    //GETTERS
    public List<PlayerRecord> getPlayers() {
        synchronized (players) {
            return new ArrayList<>(players);
        }
    }
    public int getPlayersSize(){
        synchronized (players) {
            return players.size();
        }
    }
    public String getUsernameByIndex(int index){
        return players.get(index).username();
    }





    //SETTERS
    public void setPlayers(List<PlayerRecord> players) {
        synchronized (this.players) {
            this.players.clear();
            this.players.addAll(players);
        }
        super.updateObservers();
    }
    public void setSpecificPlayer(PlayerRecord player){
        PlayerRecord currentPlayer;

        for (int i = 0; i < players.size(); i++) {
            currentPlayer = players.get(i);

            if (currentPlayer.username().equals(player.username())) {
                players.set(i, player);
                break;
            }
        }

        super.updateObservers();
    }
}
