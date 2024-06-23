package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class WaitTypeView extends LiveComponent{

    public WaitTypeView() {
        super();
        refreshObserved();
    }

    @Override
    public void print() {
        if (Game.getInstance().isSetupFinished()) {
                if (Game.getInstance().isWaitingForReconnections())
                    System.out.println("\nYou're the only player online. Waiting for reconnections!");
        } else {
            System.out.println("\nThe Set Up is not completed. Please wait!");
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Game.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Game.getInstance());
    }
}

