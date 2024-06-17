package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class WaitTypeView extends LiveComponent{
    private final ViewState view;

    public WaitTypeView(ViewState view) {
        super(view);
        this.view = view;
    }

    @Override
    public void print() {
        if (Game.getInstance().isSetupFinished()) {
                if (!Game.getInstance().isWaitingForReconnections()) {
                    new TurnShower(view).print();
                }
                else
                    System.out.println("\nYou're the only player online. Waiting for reconnections!");
        } else {
            System.out.println("\nThe Set Up is not completed. Please wait!");
        }
    }

    @Override
    public void cleanObserved() {

    }
}

