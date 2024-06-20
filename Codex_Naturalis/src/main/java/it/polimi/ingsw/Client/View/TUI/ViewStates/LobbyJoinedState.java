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

        lobbyView = new LobbyView();
        gameStartingStatus = new GameStartingStatus();
    }

    @Override
    public void print() {
        logger.info("Called print method");

        TextUI.clearCMD();
        TextUI.displayGameTitle();

        System.out.println("\nTo display the available commands type /help or /h");

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
