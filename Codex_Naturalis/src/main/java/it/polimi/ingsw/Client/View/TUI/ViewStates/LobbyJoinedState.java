package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ColorPicker;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.GameManualStarter;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.HashMap;
import java.util.Map;

public class LobbyJoinedState extends LobbyStates {
    Component lobbyView;
    InteractiveComponent mainComponent;

    boolean attemptToExitMainComponent;
    boolean commandNotFund;

    public LobbyJoinedState(ClientModel2 model) {
        super(model);

        lobbyView = new LobbyView(this);
        mainComponent = new GameManualStarter(this);
        addSecondaryComponent(new ColorPicker(this));
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        lobbyView.print();

        super.print();
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
            return true;
        }
        return false;
    }
}
