package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

/**
 * The WaitTypeView class is a live component that displays the current waiting status of the game.
 * It observes the game state and updates its display based on whether the setup is finished or if the game is waiting for reconnections.
 */
public class WaitTypeView extends LiveComponent {

    /**
     * Constructs a new WaitTypeView and initializes the observed data.
     */
    public WaitTypeView() {
        super();
        refreshObserved();
    }

    /**
     * Prints the current waiting status of the game.
     * If the game setup is not completed, it informs the user to wait.
     * If the setup is finished but waiting for reconnections, it informs the user accordingly.
     */
    @Override
    public void print() {
        if (Game.getInstance().isSetupFinished()) {
            if (Game.getInstance().isWaitingForReconnections()) {
                System.out.println("\nYou're the only player online. Waiting for reconnections!");
            }
        } else {
            System.out.println("\nThe Set Up is not completed. Please wait!");
        }
    }

    /**
     * Cleans the observed data of this component by removing it from the RefreshManager's list of observed objects.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Game.getInstance());
    }

    /**
     * Refreshes the observed data of this component by adding it to the RefreshManager's list of observed objects.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Game.getInstance());
    }
}
