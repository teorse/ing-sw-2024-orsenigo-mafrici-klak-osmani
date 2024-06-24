package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.PickSecretObjective;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.ObjectiveCandidatesView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

/**
 * Represents the state of the game where the player picks a secret objective.
 */
public class GamePickObjectiveState extends GameState {
    private final LiveComponent objectiveCandidates;
    private final Logger logger;

    /**
     * Constructs the GamePickObjectiveState and initializes its components.
     */
    public GamePickObjectiveState() {
        super(new PickSecretObjective());
        logger = Logger.getLogger(GamePickObjectiveState.class.getName());

        objectiveCandidates = new ObjectiveCandidatesView();
        refreshObservables();
    }

    /**
     * Prints the game pick objective state to the console.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            if(!Game.getInstance().isLastRoundFlag())
                TextUI.displayGameTitle();
            else
                TextUI.displayLastRound();

            objectiveCandidates.print();
            getActiveComponent().print();
            super.print();
        }
    }

    /**
     * Refreshes the observables for the components in this state.
     */
    @Override
    public void refreshObservables() {
        super.refreshObservables();
        objectiveCandidates.refreshObserved();
    }

    /**
     * Updates the state, checking for transitions to the next state. If no transition occurs, it reprints the current view.
     */
    @Override
    public void update() {
        logger.fine("Updating in GamePickObjectiveState");
        if(!nextState())
            ClientModel.getInstance().printView();
        logger.fine("finished updating in GamePickObjectiveState");
    }
}