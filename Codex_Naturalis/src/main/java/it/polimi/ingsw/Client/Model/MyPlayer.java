package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.logging.Logger;

public class MyPlayer extends Observable{
    //SINGLETON PATTERN
    private static MyPlayer INSTANCE;
    private MyPlayer(){
        super();

        logger = Logger.getLogger(MyPlayer.class.getName());
        logger.info("Initializing MyPlayer class.");

        username = null;
        isAdmin = false;
        newState = false;
    }
    public static MyPlayer getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MyPlayer();
        }
        return INSTANCE;
    }





    //ATTRIBUTES
    private String username;
    private boolean isAdmin;

    private final Object PlayerGameStateLock = new Object();
    private PlayerStates myPlayerGameState;
    private boolean newState;

    private final Logger logger;





    //GETTERS
    public String getUsername() {
        return username;
    }
    public boolean isAdmin() { return isAdmin; }
    public boolean isNewState(){
        return newState;
    }
    public PlayerStates getMyPlayerGameState() {
        synchronized (PlayerGameStateLock) {
            newState = false;
            return myPlayerGameState;
        }
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
        logger.info("Setting My Player Game State to: "+myPlayerGameState);

        synchronized (PlayerGameStateLock) {
            this.myPlayerGameState = myPlayerGameState;
            newState = true;

            logger.fine("Updating observers");
            super.updateObservers();
        }
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
