package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

/**
 * The ObjectiveView class represents a component responsible for displaying
 * details of an objective record in the TUI.
 */
public class ObjectiveView extends Component {

    private final ObjectiveRecord objective;

    /**
     * Constructs a new ObjectiveView instance with the given ObjectiveRecord.
     * @param objective The objective record to display.
     */
    public ObjectiveView(ObjectiveRecord objective){
        this.objective = objective;
    }

    /**
     * Prints the details of the objective record to the terminal.
     * Displays the description and points associated with the objective.
     */
    public void print(){
        out.println("Description: " + objective.description());
        out.println("Points: " + objective.points());
    }
}
