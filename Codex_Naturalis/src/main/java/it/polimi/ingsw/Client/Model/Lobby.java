package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.List;

public class Lobby extends Observable {
    //SINGLETON PATTERN
    private static Lobby INSTANCE;
    private Lobby(){}
    public static Lobby getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Lobby();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private String lobbyName;
    private int targetNumberUsers;
    private List<LobbyUserColors> availableUserColors;
    private boolean gameStartable;
    private LobbyUsers lobbyUsers;





    //GETTERS
    public String getLobbyName() {
        return lobbyName;
    }
    public int getTargetNumberUsers() {
        return targetNumberUsers;
    }
    public List<LobbyUserColors> getAvailableUserColors() {
        return availableUserColors;
    }
    public boolean isGameStartable() {
        return gameStartable;
    }
    public LobbyUsers getLobbyUsers() {
        return lobbyUsers;
    }





    //SETTERS
    public void setLobby(LobbyRecord lobby){
        setLobbyName(lobby.lobbyName());
        setTargetNumberUsers(lobby.targetNumberUsers());
        setAvailableUserColors(lobby.availableUserColors());
        setGameStartable(lobby.gameStartable());
        super.updateObservers();
    }
    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
        super.updateObservers();
    }
    public void setTargetNumberUsers(int targetNumberUsers) {
        this.targetNumberUsers = targetNumberUsers;
        super.updateObservers();
    }
    public void setAvailableUserColors(List<LobbyUserColors> availableUserColors) {
        this.availableUserColors = availableUserColors;
        super.updateObservers();
    }
    public void setGameStartable(boolean gameStartable) {
        this.gameStartable = gameStartable;
        super.updateObservers();
    }
}
