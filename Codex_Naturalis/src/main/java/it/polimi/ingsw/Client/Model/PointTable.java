package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.View.TUI.Components.ObjectiveView;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;

import java.util.List;

public class PointTable extends Observable {
    //SINGLETON PATTERN
    private static PointTable INSTANCE;
    private PointTable(){}
    public static PointTable getInstance(){
        if(INSTANCE == null){
            INSTANCE = new PointTable();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private List<PlayerRecord> players;
    private List<LobbyUserRecord> lobbyUserRecords;





    //GETTERS
    public List<PlayerRecord> getPlayers() {
        return players;
    }
    public List<LobbyUserRecord> getLobbyUserRecords() {
        return lobbyUserRecords;
    }
    public PlayerRecord getPlayerRecordByIndex(int index) {
        return players.get(index);
    }
    public LobbyUserRecord getLobbyUserRecordByIndex(int index) {
        return lobbyUserRecords.get(index);
    }





    //SETTERS
    public void setPlayers(List<PlayerRecord> players) {
        this.players = players;
        super.updateObservers();
    }
    public void setLobbyUserRecords(List<LobbyUserRecord> lobbyUserRecords) {
        this.lobbyUserRecords = lobbyUserRecords;
        super.updateObservers();
    }
}
