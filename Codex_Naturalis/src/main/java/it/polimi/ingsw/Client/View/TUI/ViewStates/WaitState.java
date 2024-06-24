package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.Zoomer;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents the state where players are waiting for their turn or for the game setup to complete.
 * This state includes components for displaying notifications, objectives, scoreboard, and game status.
 */
public class WaitState extends GameState {
    private List<LiveComponent> passiveComponents;
    private List<LiveComponent> postSetupComponents;

    private final Logger logger;

    /**
     * Constructs a WaitState with a Zoomer as the main component.
     * Initializes passive and post-setup components for displaying game information.
     */
    public WaitState() {
        super(new Zoomer());
        logger = Logger.getLogger(WaitState.class.getName());
        logger.info("Initializing WaitState");

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification());
        passiveComponents.add(new WaitTypeView());

        postSetupComponents = new ArrayList<>();
        postSetupComponents.add(new SharedObjectiveView());
        postSetupComponents.add(new SecretObjectiveView());
        postSetupComponents.add(new ScoreBoardView());
        postSetupComponents.add(new CardMapView());
        postSetupComponents.add(new TurnShower());

        refreshObservables();
        logger.fine("WaitState initialized");
    }

    /**
     * Prints the current state including passive components, post-setup components if setup is finished,
     * the active component, and any super components.
     */
    @Override
    public void print() {
        logger.info("Printing WaitState");

        synchronized (printLock) {
            TextUI.clearCMD();
            if (!Game.getInstance().isLastRoundFlag())
                TextUI.displayGameTitle();
            else
                TextUI.displayLastRound();

            if (Game.getInstance().isSetupFinished()) {
                for (Component component : postSetupComponents) {
                    component.print();
                }
            }
            for (Component component : passiveComponents) {
                component.print();
            }

            getActiveComponent().print();
            super.print();
        }
    }

    /**
     * Refreshes the observables for the WaitState, including both passive and post-setup components.
     */
    @Override
    public void refreshObservables() {
        logger.fine("Refreshing observables in WaitState");

        super.refreshObservables();
        for (LiveComponent component : passiveComponents) {
            component.refreshObserved();
        }
        for (LiveComponent component : postSetupComponents) {
            component.refreshObserved();
        }
    }

    /**
     * Updates the state and checks if there is a next state to transition to.
     * If no state transition occurs, prints the current view using ClientModel.
     */
    @Override
    public void update() {
        logger.info("Updating WaitState");
        if (!nextState()) {
            logger.fine("No state was found to switch to from WaitState, proceeding to call model.print");
            ClientModel.getInstance().printView();
        }
        logger.fine("Finished updating in WaitState");
    }
}