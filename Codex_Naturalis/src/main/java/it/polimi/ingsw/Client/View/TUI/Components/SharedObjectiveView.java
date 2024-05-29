package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.SharedObjectives;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class SharedObjectiveView extends Component{
    private final List<ObjectiveRecord> sharedObjectives;

    public SharedObjectiveView() {
        this.sharedObjectives = SharedObjectives.getInstance().getSharedObjectives();
    }

    @Override
    public void print() {
        System.out.println("Shared Objectives:\n");
        for (ObjectiveRecord objectiveRecord : sharedObjectives) {
            new ObjectiveView(objectiveRecord).print();
            out.println();
        }
    }
}
