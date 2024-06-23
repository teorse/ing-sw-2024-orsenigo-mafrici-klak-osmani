package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LobbyChooser;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class LobbySelectionState extends InteractiveState {

    public LobbySelectionState() {
        super(new LobbyChooser());
    }

    @Override
    public synchronized void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        super.print();

        getMainComponent().print();
    }

    @Override
    public synchronized void update() {
        if(!nextState())
            ClientModel.getInstance().printView();
    }

    synchronized boolean nextState() {
        ClientModel model = ClientModel.getInstance();

        if (model.getView().equals(this)) {
            if (super.nextState())
                return true;

            if (model.isInLobby()) {
                getMainComponent().cleanObserved();
                RefreshManager.getInstance().resetObservables();
                model.setView(new LobbyJoinedState());

                model.printView();
                return true;
            } else
                return false;
        }
        return true;
    }
}