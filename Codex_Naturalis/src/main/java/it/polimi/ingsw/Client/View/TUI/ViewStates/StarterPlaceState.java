package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardStarterChoice;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

public class StarterPlaceState extends GameState {
    private final Logger logger;

    public StarterPlaceState() {
        super(new CardStarterChoice());
        logger = Logger.getLogger(StarterPlaceState.class.getName());
        refreshObservables();
    }

    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            TextUI.displayGameTitle();

            getActiveComponent().print();
            super.print();
        }
    }

    @Override
    public void refreshObservables() {
        super.refreshObservables();
        RefreshManager.getInstance().addObserved(Game.getInstance());
    }

    @Override
    public void update() {
        logger.info("Updating StarterPlaceState");
        if(!nextState())
            ClientModel.getInstance().printView();
        logger.fine("finished updating in StarterPlaceState");
    }

    public boolean nextState() {
        synchronized (nextStateLock) {
            if(ClientModel.getInstance().getView().equals(this)) {
                if (super.nextState())
                    return true;
                if(Game.getInstance().isSetupFinished()){
                    RefreshManager.getInstance().resetObservables();
                    ClientModel.getInstance().setView(new PlaceState());
                    ClientModel.getInstance().printView();
                    return true;
                }
                return false;
            }
            return true;
        }
    }
}
