package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.GameOverView;

public class GameOverState extends ViewState {

    Component passiveComponent;

    public GameOverState(ClientModel2 model) {
        super(model);
        passiveComponent = new GameOverView();
    }

    @Override
    public void print() {
        passiveComponent.print();
    }

    @Override
    public void handleInput(String input) {
       update();
    }

    @Override
    public void update() {
        model.setView(new LobbyJoinedState(model));
    }
}
