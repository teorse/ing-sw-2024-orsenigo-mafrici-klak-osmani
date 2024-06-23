package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.List;

public class GameOver extends Observable{
    //SINGLETON PATTERN
    private static GameOver INSTANCE;
    private GameOver(){}
    public synchronized static GameOver getInstance(){
        if(INSTANCE == null){
            INSTANCE = new GameOver();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private List<PlayerRecord> winners;





    //GETTERS
    public List<PlayerRecord> getWinners() {
        return winners;
    }
    public PlayerRecord getWinnerByIndex(int index) {
        return winners.get(index);
    }
    public PlayerRecord getSpeficWinner(String username) {
        for (PlayerRecord playerRecord : winners) {
            if (playerRecord.username().equals(username)) {
                return playerRecord;
            }
        }
        return null;
    }





    //SETTERS
    public void setWinners(List<PlayerRecord> winners) {
        this.winners = winners;
        super.updateObservers();
    }
}
