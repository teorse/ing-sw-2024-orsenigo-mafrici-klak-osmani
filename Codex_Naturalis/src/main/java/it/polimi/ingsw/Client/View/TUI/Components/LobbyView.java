package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;

import java.util.logging.Logger;

public class LobbyView extends LiveComponent {
    private final LobbyUsers lobbyUsers;
    private final Logger logger;

    public LobbyView() {
        super();
        logger = Logger.getLogger(LobbyView.class.getName());
        logger.info("Initializing LobbyView component.");

        this.lobbyUsers = LobbyUsers.getInstance();
        refreshObserved();
    }

    @Override
    public void print() {
        logger.info("Printing LobbyView component.");

        System.out.println("\nList of users in the lobby: ");

        if(lobbyUsers.getLobbyUserRecords() != null) {
            logger.fine("Users in lobby: ");
            for (LobbyUserRecord user : lobbyUsers.getLobbyUserRecords()) {
                logger.fine(user.username());
                new LobbyUserView(user).print();
            }
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, lobbyUsers);
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, lobbyUsers);
    }
}
