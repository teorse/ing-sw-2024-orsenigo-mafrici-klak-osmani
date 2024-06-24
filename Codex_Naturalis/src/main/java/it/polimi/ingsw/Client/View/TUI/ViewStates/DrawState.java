package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.*;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents the state where players can draw cards during the game.
 */
public class DrawState extends GameState {
    List<LiveComponent> passiveComponents;

    private final Logger logger;

    /**
     * Constructs a DrawState, initializing the main and secondary components.
     */
    public DrawState() {
        super(new CardDrawer(), new ArrayList<>(){{add(new Zoomer());}});
        logger = Logger.getLogger(DrawState.class.getName());
        logger.info("Initializing Draw State");

        passiveComponents = new ArrayList<>();

        if (Game.getInstance().isSetupFinished()) {
            passiveComponents.add(new SharedObjectiveView());
            passiveComponents.add(new SecretObjectiveView());
            passiveComponents.add(new ScoreBoardView());
            passiveComponents.add(new CardMapView());
        }
        passiveComponents.add(new ChatNotification());

        refreshObservables();
        logger.fine("Draw State Initialized");
    }

    /**
     * Prints the current state to the console, including clearing the command line and displaying relevant game information.
     */
    @Override
    public void print() {
        logger.info("Printing Draw State");

        synchronized (printLock) {
            TextUI.clearCMD();
            if (!Game.getInstance().isLastRoundFlag())
                TextUI.displayGameTitle();
            else
                TextUI.displayLastRound();

            for (Component component : passiveComponents) {
                component.print();
            }

            getActiveComponent().print();
            super.print();
        }
    }

    /**
     * Refreshes the observables for all components in this state.
     */
    @Override
    public void refreshObservables() {
        logger.info("Refreshing observables in Draw State");
        super.refreshObservables();
        for (LiveComponent component : passiveComponents) {
            component.refreshObserved();
        }
    }

    /**
     * Updates the state, checking for transitions to the next state. If no transition occurs, it reprints the current view.
     */
    @Override
    public void update() {
        logger.info("Received update signal in Draw State, evaluating next state");
        if (!nextState()) {
            logger.fine("No next state found, calling model.print method");
            ClientModel.getInstance().printView();
        }
        logger.fine("Finished updating in Draw State");
    }
}