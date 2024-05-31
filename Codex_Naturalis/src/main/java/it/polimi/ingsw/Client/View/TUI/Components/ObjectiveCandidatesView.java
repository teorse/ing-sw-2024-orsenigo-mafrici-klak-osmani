package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ObjectiveCandidates;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class ObjectiveCandidatesView extends Component {
    private List<ObjectiveRecord> objectiveCandidates;

    public ObjectiveCandidatesView() {
        this.objectiveCandidates = ObjectiveCandidates.getInstance().getObjectiveCandidates();
    }

    @Override
    public void print() {
        int i = 1;
        for (ObjectiveRecord objectiveRecord : objectiveCandidates) {
            System.out.println("\n" + i + ")");
            new ObjectiveView(objectiveCandidates.get(i-1));
            i++;
        }
    }
}
