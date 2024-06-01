package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.SecretObjective;

public class SecretObjectiveView extends Component{
    private final SecretObjective secretObjective;


    public SecretObjectiveView(){
        secretObjective = SecretObjective.getInstance();
    }

    @Override
    public void print() {
        System.out.println("Secret Objective:");
        new ObjectiveView(secretObjective.getSecretObjective()).print();
        out.println();
    }

    @Override
    public void cleanUp() {

    }
}
