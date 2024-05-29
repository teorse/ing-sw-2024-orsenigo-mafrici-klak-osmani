package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;

import java.util.List;

public class Players extends Observable{
    //SINGLETON PATTERN
    private static Players INSTANCE;
    private Players(){}
    public static Players getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Players();
        }

        return INSTANCE;
    }


    private List<PlayerRecord> players;


    public void setPlayers(List<PlayerRecord> players) {
        this.players = players;
        super.updateObservers();
    }

    public List<PlayerRecord> getPlayers() {
        return players;
    }

    public int getPlayersSize(){
        return players.size();
    }

    public String getUsernameByIndex(int index){
        return players.get(index).username();
    }
}
