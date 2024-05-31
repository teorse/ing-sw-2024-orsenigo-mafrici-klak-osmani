package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

public class MyPlayer extends Observable{
    //SINGLETON PATTERN
    private static MyPlayer INSTANCE;
    PlayerStates myPlayerGameState;



    private MyPlayer(){}
    public static MyPlayer getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MyPlayer();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private String username;
    private boolean isAdmin;





    //GETTERS
    public String getUsername() {
        return username;
    }
    public boolean isAdmin() { return isAdmin; }

    public PlayerStates getMyPlayerGameState() {
        return myPlayerGameState;
    }





    //SETTERS
    public void setUsername(String username) {
        this.username = username;
        super.updateObservers();
    }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        super.updateObservers();
    }
    public void setMyPlayerGameState(PlayerStates myPlayerGameState) {
        this.myPlayerGameState = myPlayerGameState;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
