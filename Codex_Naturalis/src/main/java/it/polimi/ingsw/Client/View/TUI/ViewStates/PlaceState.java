package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardDrawer;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ChatMessageSender;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlaceState extends ViewState {
    List<Component> passiveComponents;
    InteractiveComponent mainComponent;
    InteractiveComponent secondaryComponent;

    private final Logger logger;

    public PlaceState(ClientModel2 model) {
        super(model);
        logger = Logger.getLogger(PlaceState.class.getName());

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification());
        passiveComponents.add(new SharedObjectiveView());
        passiveComponents.add(new SecretObjectiveView());
        passiveComponents.add(new PointTableView());
        passiveComponents.add(new CardMapView());
        passiveComponents.add(new TurnShower());

        secondaryComponent = new ChatMessageSender();

        mainComponent = new CardDrawer();
    }

    @Override
    public void print() {
        //todo add class game and logic to switch between game title and last round
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        //todo add display of all available commands

        for (Component component : passiveComponents) {
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
        if(!nextState())
            print();
    }

    private boolean nextState() {
        if(model.isGameOver()) {
            model.setView(new GameOverState(model));
            return true;
        }
        else if (MyPlayer.getInstance().getMyPlayerGameState() == PlayerStates.DRAW) {
            model.setView(new DrawState(model));
            return true;
        }
        else if (MyPlayer.getInstance().getMyPlayerGameState() == PlayerStates.WAIT) {
            model.setView(new WaitState(model));
            return true;
        }
        else
            return false;
    }
}
