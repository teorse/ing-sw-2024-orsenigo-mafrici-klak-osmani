package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Lobby;
import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.RefreshManager;

import java.util.logging.Logger;

/**
 * The GameStartingStatus class is responsible for displaying the current status
 * of the game starting process in the terminal.
 * It observes the Lobby model for changes and prints appropriate messages
 * based on the number of users in the lobby compared to the target number.
 */
public class GameStartingStatus extends LiveComponent {

    private final Logger logger;

    /**
     * Constructs a new GameStartingStatus component.
     * Initializes the logger and refreshes its observation of the Lobby model.
     */
    public GameStartingStatus() {
        super();
        logger = Logger.getLogger(GameStartingStatus.class.getName());
        logger.info("Initializing GameStartingStatus component");

        refreshObserved();
    }

    /**
     * Prints the current game starting status in the terminal.
     * Checks if the number of users in the lobby matches the target number.
     * If so, prints a message indicating that the game will start soon.
     * Otherwise, prints a message indicating that the lobby is waiting for players to join.
     */
    @Override
    public void print() {
        logger.info("Printing GameStartingStatus component");
        Lobby lobby = Lobby.getInstance();

        if (LobbyUsers.getInstance().size() == lobby.getTargetNumberUsers()) {
            System.out.println("\nTarget number of users reached. Game will start in 20 seconds.");
        } else {
            System.out.println("\nWaiting for players to join the lobby.");
        }
    }

    /**
     * Cleans up the observation of the Lobby model when this component is no longer needed.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Lobby.getInstance());
    }

    /**
     * Refreshes the observation of the Lobby model.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Lobby.getInstance());
    }
}
