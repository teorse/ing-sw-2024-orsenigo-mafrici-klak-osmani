package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LobbyChooser;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;

public class LobbySelectionState extends ViewState {
    InteractiveComponent mainComponent;


    public LobbySelectionState(ClientModel2 model) {
        super(model);
        mainComponent = new LobbyChooser();
    }

    @Override
    public void print() {
        mainComponent.print();
    }

    @Override
    public void handleInput(String input) {
        mainComponent.handleInput(input);
    }

    @Override
    public void update() {
        if(model.isInLobby())
            model.setView(new LobbyJoinedState(model));
        else
            print();
    }
}