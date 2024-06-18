package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.GameStartingStatus;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ColorPicker;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.GameManualStarter;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

public class LobbyJoinedState extends LobbyStates {
    Component lobbyView;
    Component gameStartingStatus;

    private final Logger logger;

    public LobbyJoinedState(ClientModel model) {
        super(model);
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

        super.print();
        gameStartingStatus.print();
    }

    @Override
    public void update() {
        if (!nextState())
            print();
    }

    private boolean nextState(){
        if (model.isGameStarted()) {
            model.unsubscribe(this);

            sleepOnObservables();

            switch (MyPlayer.getInstance().getMyPlayerGameState()) {
                case PLACE -> {
                    if (!Game.getInstance().isSetupFinished())
                        model.setView(new StarterPlaceState(model));
                    else
                        model.setView(new PlaceState(model));
                }
                case DRAW -> model.setView(new DrawState(model));
                case PICK_OBJECTIVE -> model.setView(new GamePickObjectiveState(model));
                case WAIT -> model.setView(new WaitState(model));
            }
            model.getView().print();
            return true;
        }
        return false;
    }
}
