package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;

import java.util.logging.Logger;

/**
 * The LobbyView class represents a component responsible for displaying
 * the list of users currently in the lobby in the TUI.
 * It observes changes in the lobby users and updates its display accordingly.
 */
public class LobbyView extends LiveComponent {

    private final LobbyUsers lobbyUsers;
    private final Logger logger;

    /**
     * Constructs a new LobbyView instance.
     * Initializes the logger and retrieves the LobbyUsers instance.
     * Registers this component as an observer of the lobby users for updates.
     */
    public LobbyView() {
        super();
        logger = Logger.getLogger(LobbyView.class.getName());
        logger.info("Initializing LobbyView component.");

        this.lobbyUsers = LobbyUsers.getInstance();
        refreshObserved();
    }

    /**
     * Prints the list of users currently in the lobby to the terminal.
     * It iterates through the lobby user records and prints each user's details using LobbyUserView.
     */
    @Override
    public void print() {
        logger.info("Printing LobbyView component.");

        System.out.println("\nList of users in the lobby: ");

        if (lobbyUsers.getLobbyUserRecords() != null) {
            logger.fine("Users in lobby: ");
            for (LobbyUserRecord user : lobbyUsers.getLobbyUserRecords()) {
                logger.fine(user.username());
                new LobbyUserView(user).print();
            }
        }
    }

    /**
     * Cleans up the observed relationship between this component and the lobby users.
     * Removes this component from the list of observers registered with RefreshManager.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, lobbyUsers);
    }

    /**
     * Registers this component as an observer of the lobby users for updates.
     * Adds this component to the list of observers managed by RefreshManager.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, lobbyUsers);
    }
}
