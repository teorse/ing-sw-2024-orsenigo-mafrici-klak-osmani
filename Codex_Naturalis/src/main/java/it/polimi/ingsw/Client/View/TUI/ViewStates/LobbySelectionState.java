package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LobbyChooser;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;

public class LobbySelectionState extends ViewState {
    InteractiveComponent mainComponent;


    public LobbySelectionState(ClientModel2 model) {
        super(model);
        mainComponent = new LobbyChooser(this);
    }

    @Override
    public void print() {
        mainComponent.print();
    }

    @Override
    public boolean handleInput(String input) {
        mainComponent.handleInput(input);
        return true;
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }

    private boolean nextState(){
        if(model.isInLobby()) {
            model.unsubscribe(this);
            model.setView(new LobbyJoinedState(model));
            return true;
        }
        else
            return false;
    }
}