package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.Model.SecretObjective;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

import java.util.List;

public class SecretObjectiveView extends Component{
    private final SecretObjective secretObjective;


    public SecretObjectiveView(ViewState viewState){
        super(viewState);
        secretObjective = SecretObjective.getInstance();
    }

    @Override
    public void print() {
        System.out.println("Secret Objective:");
        new ObjectiveView(view, secretObjective.getSecretObjective()).print();
        out.println();
    }
}
