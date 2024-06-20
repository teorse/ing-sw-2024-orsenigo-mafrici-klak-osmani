package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.PickSecretObjective;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.ObjectiveCandidatesView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class GamePickObjectiveState extends GameState {
    private final LiveComponent objectiveCandidates;


    public GamePickObjectiveState() {
        super(new PickSecretObjective());
        objectiveCandidates = new ObjectiveCandidatesView();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        if(!Game.getInstance().isLastRoundFlag())
            TextUI.displayGameTitle();
        else
            TextUI.displayLastRound();

        System.out.println("\nTo display the available commands type /help or /h");

        getActiveComponent().print();
        super.print();
    }

    @Override
    public void refreshObservables() {
        objectiveCandidates.refreshObserved();
    }

    @Override
    public void update() {
        if(nextState())
            print();
    }
}
