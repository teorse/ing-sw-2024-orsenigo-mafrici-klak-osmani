package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyRoles;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LobbyUsers extends Observable{
    //SINGLETON PATTERN
    private static LobbyUsers INSTANCE;
    private LobbyUsers(){
        lobbyUserRecords = new ArrayList<>();
        lobbyUserColors = new HashMap<>();
        logger = Logger.getLogger(LobbyUsers.class.getName());
        logger.info("Initializing LobbyUsers Class");
    }
    public synchronized static LobbyUsers getInstance(){
        if(INSTANCE == null){
            INSTANCE = new LobbyUsers();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private List<LobbyUserRecord> lobbyUserRecords;
    private final Map<String, LobbyUserColors> lobbyUserColors;
    private final Logger logger;





    //GETTERS
    public List<LobbyUserRecord> getLobbyUserRecords() {
        logger.info("get Lobby User Records method called");
        logger.fine("Lobby User Records contents are: " + lobbyUserRecords);

        return new ArrayList<>(lobbyUserRecords);
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
        if(lobbyUserColors.containsKey(username))
            return lobbyUserColors.get(username);
        return null;
    }
    public Map<String, LobbyUserColors> getLobbyUserColorsMap(){
        return new HashMap<>(lobbyUserColors);
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
        logger.info("setLobbyUserRecords method called");

        logger.fine("Parameter lobby user records are: " +lobbyUserRecords);

        this.lobbyUserRecords = new ArrayList<>(lobbyUserRecords);

        for(LobbyUserRecord user : lobbyUserRecords){
            lobbyUserColors.put(user.username(), user.color());
        }

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

        logger.fine("Object State lobby user variable contains: "+this.lobbyUserRecords);
        super.updateObservers();
    }
}
