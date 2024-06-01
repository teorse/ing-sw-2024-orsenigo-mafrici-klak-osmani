package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.CardsHeld;
import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.CardsHeldView;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;

public class GamePickObjectiveState extends ViewState {
    Component component;
    InteractiveComponent mainComponent;




    public GamePickObjectiveState(ClientModel2 model) {
        super(model);
        component = new CardsHeldView();
        mainComponent = new P
    }

    @Override
    public void print() {

    }

    @Override
    public void handleInput(String input) {

    }

    @Override
    public void update() {

    }
}
