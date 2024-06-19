package it.polimi.ingsw.Client.View.TUI.ViewStates;

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

        lobbyView = new LobbyView(this);
        gameStartingStatus = new GameStartingStatus(this);

        logger.fine("assigning main component");
        mainComponent = new GameManualStarter(this);
        logger.fine("main component assigned");

        if(mainComponent.equals(activeComponent))
            logger.fine("Main component and active component are equal after assignement");
        else
            logger.fine("Main component and active component are NOT equal after assignement");

        addSecondaryComponent(new ColorPicker(this));
    }

    @Override
    public void print() {
        logger.info("Called print method");

        TextUI.clearCMD();
        TextUI.displayGameTitle();
        lobbyView.print();
        getActiveComponent().print();
        super.print();
        gameStartingStatus.print();
    }

    @Override
    public void refreshObservables() {
        super.refreshObservables();
        lobbyView.refreshObserved();
        gameStartingStatus.refreshObserved();
    }

    @Override
    public void update() {
        if (!nextState())
            print();
    }
}
