package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.Model.SharedObjectives;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

/**
 * The SharedObjectiveView class is responsible for displaying the shared objectives
 * in the TUI. It observes changes in the shared objectives
 * and updates its display accordingly.
 */
public class SharedObjectiveView extends LiveComponent {

    /**
     * Constructs a new SharedObjectiveView instance.
     * Initializes and sets up observation of the shared objectives.
     */
    public SharedObjectiveView() {
        super();
        refreshObserved();
    }

    /**
     * Prints the shared objectives information to the terminal.
     * Displays the description and points of each shared objective.
     */
    @Override
    public void print() {
        List<ObjectiveRecord> sharedObjectives = SharedObjectives.getInstance().getSharedObjectives();

        System.out.println("Shared Objectives:\n");

        if (sharedObjectives != null) {
            for (ObjectiveRecord objectiveRecord : sharedObjectives) {
                new ObjectiveView(objectiveRecord).print();
                out.println(); // Print a blank line for separation
            }
        }
    }

    /**
     * Removes this component from being observed by the SharedObjectives instance.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, SharedObjectives.getInstance());
    }

    /**
     * Adds this component to be observed by the SharedObjectives instance for updates.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, SharedObjectives.getInstance());
    }
}
