package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardStarterChoice;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

/**
 * Represents the state where players choose the starting cards to place on the board.
 */
public class StarterPlaceState extends GameState {
    private final Logger logger;

    /**
     * Constructs a StarterPlaceState with a CardStarterChoice as the main component.
     */
    public StarterPlaceState() {
        super(new CardStarterChoice());
        logger = Logger.getLogger(StarterPlaceState.class.getName());
        refreshObservables();
    }

    /**
     * Prints the current state including the active component and any super components.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            TextUI.displayGameTitle();

            getActiveComponent().print();
            super.print();
        }
    }

    /**
     * Refreshes the observables for the StarterPlaceState, adding Game.getInstance() as an observed object.
     */
    @Override
    public void refreshObservables() {
        super.refreshObservables();
        RefreshManager.getInstance().addObserved(Game.getInstance());
    }

    /**
     * Updates the state and checks if there is a next state to transition to.
     * If setup is finished in the game, transitions to PlaceState; otherwise, remains in StarterPlaceState.
     */
    @Override
    public void update() {
        logger.info("Updating StarterPlaceState");
        if(!nextState())
            ClientModel.getInstance().printView();
        logger.fine("Finished updating in StarterPlaceState");
    }

    /**
     * Determines the next state to transition to based on game setup status.
     * @return true if a state transition occurs, false otherwise.
     */
    public boolean nextState() {
        synchronized (nextStateLock) {
            if(ClientModel.getInstance().getView().equals(this)) {
                if (super.nextState())
                    return true;
                if(Game.getInstance().isSetupFinished()){
                    RefreshManager.getInstance().resetObservables();
                    ClientModel.getInstance().setView(new PlaceState());
                    ClientModel.getInstance().printView();
                    return true;
                }
                return false;
            }
            return true;
        }
    }
}