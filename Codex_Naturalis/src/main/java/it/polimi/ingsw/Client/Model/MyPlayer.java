package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.logging.Logger;

/**
 * Singleton class representing the current player's state and information in the client-side model.
 */
public class MyPlayer extends Observable {
    // Singleton instance
    private static MyPlayer INSTANCE;

    // Attributes
    private String username;
    private boolean isAdmin;
    private final Object PlayerGameStateLock = new Object();
    private PlayerStates myPlayerGameState;
    private boolean newState;
    private final Logger logger;

    /**
     * Private constructor to enforce singleton pattern and initialize attributes.
     */
    private MyPlayer() {
        super();
        logger = Logger.getLogger(MyPlayer.class.getName());
        logger.info("Initializing MyPlayer class.");
        username = null;
        isAdmin = false;
        newState = false;
    }

    /**
     * Retrieves the singleton instance of the MyPlayer class.
     *
     * @return The singleton instance of MyPlayer.
     */
    public synchronized static MyPlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyPlayer();
        }
        return INSTANCE;
    }

    /**
     * Retrieves the username of the current player.
     *
     * @return The username of the current player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Checks if the current player is an admin.
     *
     * @return True if the player is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Checks if there is a new state update for the player.
     *
     * @return True if there is a new state update, false otherwise.
     */
    public boolean isNewState() {
        return newState;
    }

    /**
     * Retrieves the current game state of the player.
     *
     * @return The current game state of the player.
     */
    public PlayerStates getMyPlayerGameState() {
        synchronized (PlayerGameStateLock) {
            logger.info("Getting My Player Game State, current value is: " + myPlayerGameState);
            newState = false;
            return myPlayerGameState;
        }
    }

    /**
     * Sets the username of the current player.
     *
     * @param username The username to set for the player.
     */
    public void setUsername(String username) {
        this.username = username;
        super.updateObservers();
    }

    /**
     * Sets whether the current player is an admin.
     *
     * @param isAdmin True if the player is an admin, false otherwise.
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        super.updateObservers();
    }

    /**
     * Sets the game state of the current player.
     *
     * @param myPlayerGameState The game state to set for the player.
     */
    public void setMyPlayerGameState(PlayerStates myPlayerGameState) {
        synchronized (PlayerGameStateLock) {
            logger.info("Setting My Player Game State to: " + myPlayerGameState);
            this.myPlayerGameState = myPlayerGameState;
            newState = true;
            logger.fine("My Player Game State after setting is: " + this.myPlayerGameState);

            logger.fine("Updating observers");
            super.updateObservers();
        }
    }

    // Additional setter for admin status (no observer update)
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
