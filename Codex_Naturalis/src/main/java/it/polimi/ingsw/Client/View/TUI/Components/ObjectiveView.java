package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

public class ObjectiveView extends Component {

    private final ObjectiveRecord objective;

    public ObjectiveView(ObjectiveRecord objective){
        this.objective = objective;

    }

    public void print(){
        out.println("Description: " + objective.description());
        out.println("Points: " + objective.points());
    }
}
