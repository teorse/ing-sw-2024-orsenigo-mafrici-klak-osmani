package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

public class ObjectiveView extends Component {

    private final ObjectiveRecord objective;

    public ObjectiveView(ViewState viewState, ObjectiveRecord objective){
        super(viewState);
        this.objective = objective;
    }

    public void print(){
        out.println("Description: " + objective.description());
        out.println("Points: " + objective.points());
    }
}
