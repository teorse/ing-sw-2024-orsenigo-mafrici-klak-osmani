package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardStarterChoice;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class StarterPlaceState extends GameState {


    public StarterPlaceState() {
        super(new CardStarterChoice());
        refreshObservables();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        getActiveComponent().print();
        super.print();
    }

    @Override
    public void refreshObservables() {
        super.refreshObservables();
    }

    @Override
    public void update() {
        if(!nextState())
            ClientModel.getInstance().printView();
    }

    public synchronized boolean nextState() {
        if(ClientModel.getInstance().getView().equals(this)) {
            if (super.nextState())
                return true;
            if(Game.getInstance().isSetupFinished()){
                RefreshManager.getInstance().resetObservables();
                ClientModel.getInstance().setView(new PlaceState());
                ClientModel.getInstance().printView();
                return true;
            }
        }
        return false;
    }
}
