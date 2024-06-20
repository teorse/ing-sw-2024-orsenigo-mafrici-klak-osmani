package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Lobby;
import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.RefreshManager;

import java.util.logging.Logger;

public class GameStartingStatus extends LiveComponent{

    private final Logger logger;

    public GameStartingStatus() {
        super();
        logger = Logger.getLogger(GameStartingStatus.class.getName());
        logger.info("Initializing GameStartingStatus component");

        refreshObserved();
    }

    @Override
    public void print() {
        logger.info("Printing GameStartingStatus component");
        Lobby lobby = Lobby.getInstance();

        if(LobbyUsers.getInstance().size() == lobby.getTargetNumberUsers()) {
            System.out.println("\nTarget number of users reached. Game will start in 20 seconds.");
        }
        else {
            System.out.println("\nWaiting for players to join the lobby.");
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Lobby.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Lobby.getInstance());
    }
}
