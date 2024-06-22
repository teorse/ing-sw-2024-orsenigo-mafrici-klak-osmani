package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyRoles;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public int size() {
        if(lobbyUserRecords != null)
            return lobbyUserRecords.size();
        else
            return 0;
    }
    public LobbyUserColors getLobbyUserColors(String username) {
        for (LobbyUserRecord lobbyUserRecord : lobbyUserRecords) {
            if (lobbyUserRecord.username().equals(username)) {
                return lobbyUserRecord.color();
            }
        }
        return null;
    }
    public Map<String, LobbyUserColors> getLobbyUserColorsMap(){
        Map<String, LobbyUserColors> map = new HashMap<>();
        for(LobbyUserRecord lobbyUser : lobbyUserRecords){
            map.put(lobbyUser.username(), lobbyUser.color());
        }
        return map;
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

        for(LobbyUserRecord user : lobbyUserRecords){
            if(user.username().equals(MyPlayer.getInstance().getUsername())) {
                if(user.role().equals(LobbyRoles.ADMIN)){
                    MyPlayer.getInstance().setIsAdmin(true);
                }
                else{
                    MyPlayer.getInstance().setIsAdmin(false);
                }
                break;
            }
        }

        super.updateObservers();
    }
}
