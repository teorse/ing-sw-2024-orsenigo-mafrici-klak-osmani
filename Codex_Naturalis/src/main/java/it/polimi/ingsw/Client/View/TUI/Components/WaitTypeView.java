package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

import java.util.List;

public class WaitTypeView extends Component{

    private ClientModel model;

    public WaitTypeView(ViewState viewState) {
        super(viewState);
        this.model = ClientModel.getInstance();
    }

    @Override
    public void print() {
        if (model.isSetUpFinished()) {
                if (!model.isWaitingForReconnections()) {
                    new TurnShower(view).print();
                }
                else
                    System.out.println("\nYou're the only player online. Waiting for reconnections!");
        } else {
            System.out.println("\nThe Set Up is not completed. Please wait!");
        }
    }
}

