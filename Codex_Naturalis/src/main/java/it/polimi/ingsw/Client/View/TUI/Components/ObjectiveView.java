package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

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

    @Override
    public void cleanUp() {

    }
}
