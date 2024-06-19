package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.Model.SharedObjectives;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class SharedObjectiveView extends LiveComponent{
    public SharedObjectiveView() {
        super();
        refreshObserved();
    }

    @Override
    public void print() {
        List<ObjectiveRecord> sharedObjectives = SharedObjectives.getInstance().getSharedObjectives();

        System.out.println("Shared Objectives:\n");
        for (ObjectiveRecord objectiveRecord : sharedObjectives) {
            new ObjectiveView(objectiveRecord).print();
            out.println();
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, SharedObjectives.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, SharedObjectives.getInstance());
    }
}
