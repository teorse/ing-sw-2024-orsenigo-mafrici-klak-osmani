package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.Model.SecretObjective;

/**
 * The SecretObjectiveView class is responsible for displaying the secret objective
 * in the TUI. It observes changes in the secret objective
 * and updates its display accordingly.
 */
public class SecretObjectiveView extends LiveComponent {

    /**
     * Constructs a new SecretObjectiveView instance.
     * Initializes and sets up observation of the secret objective.
     */
    public SecretObjectiveView() {
        super();
        refreshObserved();
    }

    /**
     * Prints the secret objective information to the terminal.
     * Displays the description and points of the secret objective if it exists.
     */
    @Override
    public void print() {
        SecretObjective secretObjective = SecretObjective.getInstance();
        System.out.println("Secret Objective:");
        if (secretObjective.getSecretObjective() != null) {
            new ObjectiveView(secretObjective.getSecretObjective()).print();
            out.println(); // Print a blank line for separation
        }
    }

    /**
     * Removes this component from being observed by the SecretObjective instance.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, SecretObjective.getInstance());
    }

    /**
     * Adds this component to be observed by the SecretObjective instance for updates.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, SecretObjective.getInstance());
    }
}
