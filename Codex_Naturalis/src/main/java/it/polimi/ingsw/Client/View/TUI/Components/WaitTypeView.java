package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.Zoomer;

public class WaitTypeView extends Component{

    private ClientModel model;

    public WaitTypeView() {
        this.model = ClientModel.getInstance();
    }

    @Override
    public void print() {
        if (model.isSetUpFinished()) {
                if (!model.isWaitingForReconnections()) {
                    new TurnShower().print();
                }
                else
                    System.out.println("\nYou're the only player online. Waiting for reconnections!");
        } else {
            System.out.println("\nThe Set Up is not completed. Please wait!");
        }
    }
}

