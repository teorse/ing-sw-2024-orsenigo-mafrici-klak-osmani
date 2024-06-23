package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LobbyChooser;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

public class LobbySelectionState extends InteractiveState {
    private final Logger logger;

    public LobbySelectionState() {
        super(new LobbyChooser());
        logger = Logger.getLogger(LobbySelectionState.class.getName());
    }

    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            TextUI.displayGameTitle();

            super.print();

            getMainComponent().print();
        }
    }

    @Override
    public void update() {
        logger.fine("Updating in LobbySelectionState");
        if(!nextState())
            ClientModel.getInstance().printView();
        logger.fine("finished updating in LobbySelectionState");
    }

    boolean nextState() {
        synchronized (nextStateLock) {
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
}