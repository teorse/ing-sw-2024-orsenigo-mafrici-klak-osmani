package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LobbyChooser;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class LobbySelectionState extends InteractiveState {

    public LobbySelectionState(ClientModel model) {
        super(model);
        mainComponent = new LobbyChooser(this);
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        super.print();

        mainComponent.print();
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

            model.getView().print();
            return true;
        }
        else
            return false;
    }
}