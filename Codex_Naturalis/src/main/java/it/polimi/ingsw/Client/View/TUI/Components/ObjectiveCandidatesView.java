package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ObjectiveCandidates;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class ObjectiveCandidatesView extends LiveComponent {

    public ObjectiveCandidatesView(ViewState view) {
        super(view);
        view.addObserved(ObjectiveCandidates.getInstance());
    }

    @Override
    public void print() {
        List<ObjectiveRecord> objectiveCandidates = ObjectiveCandidates.getInstance().getObjectiveCandidates();
        int i = 1;
        for (ObjectiveRecord objectiveRecord : objectiveCandidates) {
            System.out.println("\n" + i + ")");
            new ObjectiveView(objectiveCandidates.get(i-1)); //todo What is this strange for loop???
            i++;
        }
    }

    @Override
    public void cleanObserved() {
        view.removeObserved(ObjectiveCandidates.getInstance());
    }
}
