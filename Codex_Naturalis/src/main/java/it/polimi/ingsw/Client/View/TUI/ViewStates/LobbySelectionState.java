package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LobbyChooser;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class LobbySelectionState extends InteractiveState {

    public LobbySelectionState() {
        super(new LobbyChooser());
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        super.print();

        getMainComponent().print();
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }

    boolean nextState(){
        ClientModel model = ClientModel.getInstance();

        if(super.nextState())
            return true;

        if(model.isInLobby()) {
            getMainComponent().cleanObserved();
            RefreshManager.getInstance().resetObservables();
            model.setView(new LobbyJoinedState());

            model.getView().print();
            return true;
        }
        else
            return false;
    }
}