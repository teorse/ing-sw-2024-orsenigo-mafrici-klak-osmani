package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.*;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DrawState extends LobbyStates {
    List<Component> passiveComponenets;
    InteractiveComponent mainComponent;
    InteractiveComponent secondaryComponent;

    PlayerStates myPlayerStates;

    private final Logger logger;

    public DrawState(ClientModel2 model) {
        super(model);
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponenets = new ArrayList<>();
        passiveComponenets.add(new ChatNotification());
        passiveComponenets.add(new SharedObjectiveView());
        passiveComponenets.add(new SecretObjectiveView());
        passiveComponenets.add(new PointTableView());
        passiveComponenets.add(new CardMapView());
        passiveComponenets.add(new TurnShower());

        secondaryComponent = new ChatMessageSender(this);

        mainComponent = new CardDrawer();

        myPlayerStates = MyPlayer.getInstance().getMyPlayerGameState();
    }

    @Override
    public void print() {
        //todo add class game and logic to switch between game title and last round
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        //todo add display of all available commands

        for (Component component : passiveComponenets) {
            component.print();
        }
        mainComponent.print();
    }

    @Override
    public void handleInput(String input) {
        mainComponent.handleInput(input);
    }

    @Override
    public void update() {
        nextState();
    }

    private void nextState() {
        if(model.isGameOver())
            model.setView(new GameOverState(model));
        else if (myPlayerStates == PlayerStates.WAIT)
            model.setView(new WaitState(model));
        else
            print();
    }
}
