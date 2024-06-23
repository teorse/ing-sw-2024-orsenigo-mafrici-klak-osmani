package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
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
        refreshObservables();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        if(!Game.getInstance().isLastRoundFlag())
            TextUI.displayGameTitle();
        else
            TextUI.displayLastRound();

        objectiveCandidates.print();
        getActiveComponent().print();
        super.print();
    }

    @Override
    public void refreshObservables() {
        super.refreshObservables();
        objectiveCandidates.refreshObserved();
    }

    @Override
    public void update() {
        if(!nextState())
            ClientModel.getInstance().printView();
    }
}
