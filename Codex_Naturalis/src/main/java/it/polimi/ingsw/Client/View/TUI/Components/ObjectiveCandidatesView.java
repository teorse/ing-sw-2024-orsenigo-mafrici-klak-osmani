package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ObjectiveCandidates;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

/**
 * The ObjectiveCandidatesView class represents a component responsible for displaying
 * the list of objective candidates available in the TUI.
 * It observes changes in the objective candidates and updates its display accordingly.
 */
public class ObjectiveCandidatesView extends LiveComponent {

    /**
     * Constructs a new ObjectiveCandidatesView instance.
     * Initializes the component and registers it as an observer of the objective candidates.
     */
    public ObjectiveCandidatesView() {
        super();
        refreshObserved();
    }

    /**
     * Prints the list of objective candidates to the terminal.
     * It iterates through the objective candidates and prints each objective using ObjectiveView.
     */
    @Override
    public void print() {
        List<ObjectiveRecord> objectiveCandidates = ObjectiveCandidates.getInstance().getObjectiveCandidates();
        int i = 1;
        for (ObjectiveRecord objectiveRecord : objectiveCandidates) {
            System.out.println("\n" + i + ")");
            ObjectiveView objectiveView = new ObjectiveView(objectiveRecord);
            objectiveView.print();
            i++;
        }
    }

    /**
     * Cleans up the observed relationship between this component and the objective candidates.
     * Removes this component from the list of observers registered with RefreshManager.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, ObjectiveCandidates.getInstance());
    }

    /**
     * Registers this component as an observer of the objective candidates for updates.
     * Adds this component to the list of observers managed by RefreshManager.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, ObjectiveCandidates.getInstance());
    }
}
