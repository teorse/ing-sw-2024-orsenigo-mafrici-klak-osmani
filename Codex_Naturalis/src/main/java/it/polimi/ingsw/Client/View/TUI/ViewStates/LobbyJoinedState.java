package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.GameStartingStatus;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ColorPicker;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.GameManualStarter;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.logging.Logger;

public class LobbyJoinedState extends LobbyStates {
    LiveComponent lobbyView;
    LiveComponent gameStartingStatus;

    private final Logger logger;

    public LobbyJoinedState() {
        super(new GameManualStarter(), new ArrayList<>() {{add(new ColorPicker());}});
        logger = Logger.getLogger(LobbyJoinedState.class.getName());
        logger.info("Initializing LobbyJoinedState");

        lobbyView = new LobbyView();
        gameStartingStatus = new GameStartingStatus();

        refreshObservables();
    }

    @Override
    public void print() {
        logger.info("Called print method");

        synchronized (printLock) {
            if (ClientModel.getInstance().getView().equals(this)) {
                TextUI.clearCMD();
                TextUI.displayGameTitle();

                logger.fine("Calling lobbyView print");
                lobbyView.print();
                getActiveComponent().print();
                super.print();
                gameStartingStatus.print();
            }
        }
    }

    @Override
    public void refreshObservables() {
        super.refreshObservables();
        lobbyView.refreshObserved();
        gameStartingStatus.refreshObserved();
    }

    @Override
    public void update() {
        logger.info("Updating LobbyJoinedState");

        if (!nextState()) {
            logger.fine("No next state was found for LobbyJoinedState, proceeding to call model.print method");
            ClientModel.getInstance().printView();
        }
        logger.fine("finished updating in LobbyJoinedState");
    }
}
