package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ObjectiveCandidates;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class ObjectiveCandidatesView extends LiveComponent {

    public ObjectiveCandidatesView() {
        super();
        refreshObserved();
    }

    @Override
    public void print() {
        List<ObjectiveRecord> objectiveCandidates = ObjectiveCandidates.getInstance().getObjectiveCandidates();
        int i = 1;
        for (ObjectiveRecord objectiveRecord : objectiveCandidates) {
            System.out.println("\n" + i + ")");
            ObjectiveView objectiveView = new ObjectiveView(objectiveRecord); //Creating instance ObjectiveView
            objectiveView.print(); //Print of the objective using print() method of ObjectiveView
            i++;
        }
    }

    @Override
    public void cleanObserved() {RefreshManager.getInstance().removeObserved(this, ObjectiveCandidates.getInstance());}

    @Override
    public void refreshObserved() {RefreshManager.getInstance().addObserved(this, ObjectiveCandidates.getInstance());}
}
