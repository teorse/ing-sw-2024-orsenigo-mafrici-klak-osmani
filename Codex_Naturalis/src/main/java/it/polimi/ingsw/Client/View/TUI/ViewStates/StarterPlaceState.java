package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.CardsHeldView;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardStarterChoice;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class StarterPlaceState extends GameState {
    private final LiveComponent cardsHeld;


    public StarterPlaceState() {
        super(new CardStarterChoice());
        cardsHeld = new CardsHeldView();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        cardsHeld.print();
        getActiveComponent().print();
        super.print();
    }

    @Override
    public void refreshObservables() {
        cardsHeld.refreshObserved();
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }
}
