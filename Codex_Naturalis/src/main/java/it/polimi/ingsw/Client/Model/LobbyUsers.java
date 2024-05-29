package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.List;

public class LobbyUsers extends Observable{
    //SINGLETON PATTERN
    private static LobbyUsers INSTANCE;
    private LobbyUsers(){}
    public static LobbyUsers getInstance(){
        if(INSTANCE == null){
            INSTANCE = new LobbyUsers();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private List<LobbyUserRecord> lobbyUserRecords;





    //GETTERS
    public List<LobbyUserRecord> getLobbyUserRecords() {
        return lobbyUserRecords;
    }
    public String getLobbyUserNameByIndex(int index){
        return lobbyUserRecords.get(index).username();
    }
    public int size() {return lobbyUserRecords.size();}
    public LobbyUserColors getLobbyUserColors(String username) {
        for (LobbyUserRecord lobbyUserRecord : lobbyUserRecords) {
            if (lobbyUserRecord.username().equals(username)) {
                return lobbyUserRecord.color();
            }
        }
        return null;
    }
    public LobbyUserConnectionStates getLobbyUsersConnectionState(String username) {
        for (LobbyUserRecord lobbyUserRecord : lobbyUserRecords) {
            if (lobbyUserRecord.username().equals(username)) {
                return lobbyUserRecord.connectionStatus();
            }
        }
        return null;
    }





    //SETTERS
    public void setLobbyUserRecords(List<LobbyUserRecord> lobbyUserRecords) {
        this.lobbyUserRecords = lobbyUserRecords;
    }
}
