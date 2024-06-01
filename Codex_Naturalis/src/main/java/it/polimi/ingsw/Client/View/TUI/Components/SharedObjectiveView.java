package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.Model.SharedObjectives;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class SharedObjectiveView extends Component{
    private final List<ObjectiveRecord> sharedObjectives;

    public SharedObjectiveView(ViewState viewState) {
        super(viewState);
        this.sharedObjectives = SharedObjectives.getInstance().getSharedObjectives();
    }

    @Override
    public void print() {
        System.out.println("Shared Objectives:\n");
        for (ObjectiveRecord objectiveRecord : sharedObjectives) {
            new ObjectiveView(view, objectiveRecord).print();
            out.println();
        }
    }
}
