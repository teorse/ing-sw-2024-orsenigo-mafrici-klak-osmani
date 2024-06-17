package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.SecretObjective;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class SecretObjectiveView extends LiveComponent{

    public SecretObjectiveView(ViewState view){
        super(view);
        view.addObserved(SecretObjective.getInstance());
    }

    @Override
    public void print() {
        SecretObjective secretObjective = SecretObjective.getInstance();
        System.out.println("Secret Objective:");
        new ObjectiveView(secretObjective.getSecretObjective()).print();
        out.println();
    }

    @Override
    public void cleanObserved() {
        view.removeObserved(SecretObjective.getInstance());
    }
}
