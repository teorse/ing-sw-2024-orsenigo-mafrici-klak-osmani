package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardStarterChoice;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class StarterPlaceState extends GameState {


    public StarterPlaceState() {
        super(new CardStarterChoice());
        refreshObservables();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        getActiveComponent().print();
        super.print();
    }

    @Override
    public void refreshObservables() {
        super.refreshObservables();
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }
}
