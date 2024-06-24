package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.GameStartingStatus;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ColorPicker;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.GameManualStarter;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Represents the state when a player has joined a lobby.
 */
public class LobbyJoinedState extends LobbyStates {
    LiveComponent lobbyView;
    LiveComponent gameStartingStatus;

    private final Logger logger;

    /**
     * Constructs a LobbyJoinedState with the specified components.
     */
    public LobbyJoinedState() {
        super(new GameManualStarter(), new ArrayList<>() {{
            add(new ColorPicker());
        }});
        logger = Logger.getLogger(LobbyJoinedState.class.getName());
        logger.info("Initializing LobbyJoinedState");

        lobbyView = new LobbyView();
        gameStartingStatus = new GameStartingStatus();

        refreshObservables();
    }

    /**
     * Prints the current state, including lobby information and game starting status.
     */
    @Override
    public void print() {
        logger.info("Called print method");

        synchronized (printLock) {
            if (ClientModel.getInstance().getView().equals(this)) {
                TextUI.clearCMD();
                TextUI.displayGameTitle();

                logger.fine("Calling lobbyView print");
                lobbyView.print();
                getActiveComponent().print();
                super.print();
                gameStartingStatus.print();
            }
        }
    }

    /**
     * Refreshes the observables for the current state.
     */
    @Override
    public void refreshObservables() {
        super.refreshObservables();
        lobbyView.refreshObserved();
        gameStartingStatus.refreshObserved();
    }

    /**
     * Updates the state and determines if there is a next state to transition to.
     */
    @Override
    public void update() {
        logger.info("Updating LobbyJoinedState");

        if (!nextState()) {
            logger.fine("No next state was found for LobbyJoinedState, proceeding to call model.print method");
            ClientModel.getInstance().printView();
        }
        logger.fine("Finished updating in LobbyJoinedState");
    }
}