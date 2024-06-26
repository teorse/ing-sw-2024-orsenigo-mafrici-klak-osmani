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
 * Represents the state where players are placing their cards on the board.
 */
public class PlaceState extends GameState {
    List<LiveComponent> passiveComponents;

    private final Logger logger;

    /**
     * Constructs a PlaceState with a CardPlacer as the main component and a Zoomer as a secondary component.
     */
    public PlaceState() {
        super(new CardPlacer(), new ArrayList<>(){{add(new Zoomer());}});
        logger = Logger.getLogger(PlaceState.class.getName());
        logger.info("Initializing PlaceState.");

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification());
        passiveComponents.add(new SharedObjectiveView());
        passiveComponents.add(new SecretObjectiveView());
        passiveComponents.add(new ScoreBoardView());
        passiveComponents.add(new CardMapView());
        passiveComponents.add(new TurnShower());
        passiveComponents.add(new ArtifactCounter());
        passiveComponents.add(new CardsHeldView());

        refreshObservables();
        logger.fine("Place State Initialized");
    }

    /**
     * Prints the current state including all passive components and the active component.
     */
    @Override
    public void print() {
        logger.info("Printing Place State");

        synchronized (printLock) {
            TextUI.clearCMD();
            if(!Game.getInstance().isLastRoundFlag())
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
     * Refreshes the observables for all passive components in the PlaceState.
     */
    @Override
    public void refreshObservables() {
        logger.info("Refreshing observables in PlaceState");
        super.refreshObservables();
        for(LiveComponent component : passiveComponents){
            component.refreshObserved();
        }
    }

    /**
     * Updates the state and checks if there is a next state to transition to.
     */
    @Override
    public void update() {
        logger.info("Called update method in PlaceState");
        if(!nextState()) {
            logger.fine("No state was found to switch to from Place State, proceeding to call model.print");
            ClientModel.getInstance().printView();
        }
        logger.fine("Finished updating in PlaceState");
    }
}