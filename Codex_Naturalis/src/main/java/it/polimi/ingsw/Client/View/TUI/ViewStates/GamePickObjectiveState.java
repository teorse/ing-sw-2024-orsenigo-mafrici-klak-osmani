package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.PickSecretObjective;
import it.polimi.ingsw.Client.View.TUI.Components.ObjectiveCandidatesView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class GamePickObjectiveState extends GameState {
    Component component;




    public GamePickObjectiveState(ClientModel model) {
        super(model);
        mainComponent = new PickSecretObjective(this);
        component = new ObjectiveCandidatesView(this);

        print();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        if(!Game.getInstance().isLastRoundFlag())
            TextUI.displayGameTitle();
        else
            TextUI.displayLastRound();

        super.print();
    }

    @Override
    public void update() {
        nextState();
    }
}
