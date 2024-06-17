package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.GameOverView;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;

public class GameOverState extends ViewState {

    LiveComponent passiveComponent;

    public GameOverState(ClientModel model) {
        super(model);
        passiveComponent = new GameOverView(this);

        print();
    }

    @Override
    public void print() {
        passiveComponent.print();
    }

    @Override
    public boolean handleInput(String input) {
       update();
       return true;
    }

    @Override
    public void update() {
        model.unsubscribe(this);
        passiveComponent.cleanObserved();
        sleepOnObservables();

        model.setView(new LobbyJoinedState(model));
        model.getView().print();
    }
}
