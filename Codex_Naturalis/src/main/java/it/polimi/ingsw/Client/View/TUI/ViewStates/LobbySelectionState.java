package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LobbyChooser;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

/**
 * Represents the state where the player is selecting a lobby to join.
 */
public class LobbySelectionState extends InteractiveState {
    private final Logger logger;

    /**
     * Constructs a LobbySelectionState with the LobbyChooser as the main component.
     */
    public LobbySelectionState() {
        super(new LobbyChooser());
        logger = Logger.getLogger(LobbySelectionState.class.getName());
    }

    /**
     * Prints the current state, including the lobby chooser component.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            TextUI.displayGameTitle();

            super.print();

            getMainComponent().print();
        }
    }

    /**
     * Updates the state and checks if there is a next state to transition to.
     */
    @Override
    public void update() {
        logger.fine("Updating in LobbySelectionState");
        if (!nextState())
            ClientModel.getInstance().printView();
        logger.fine("Finished updating in LobbySelectionState");
    }

    /**
     * Determines the next state based on the current conditions.
     * @return true if a next state is found and transitioned to, false otherwise.
     */
    boolean nextState() {
        synchronized (nextStateLock) {
            ClientModel model = ClientModel.getInstance();

            if (model.getView().equals(this)) {
                if (super.nextState())
                    return true;

                if (model.isInLobby()) {
                    getMainComponent().cleanObserved();
                    RefreshManager.getInstance().resetObservables();
                    model.setView(new LobbyJoinedState());

                    model.printView();
                    return true;
                } else
                    return false;
            }
            return true;
        }
    }
}