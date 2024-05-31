package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardDrawer;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ChatMessageSender;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlaceState extends ViewState {
    List<Component> passiveComponenets;
    InteractiveComponent mainComponent;
    InteractiveComponent secondaryComponent;

    private final Logger logger;

    public PlaceState(ClientModel2 model) {
        super(model);
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponenets = new ArrayList<>();
        passiveComponenets.add(new ChatNotification());
        passiveComponenets.add(new SharedObjectiveView());
        passiveComponenets.add(new SecretObjectiveView());
        passiveComponenets.add(new PointTableView());
        passiveComponenets.add(new CardMapView());
        passiveComponenets.add(new TurnShower());

        secondaryComponent = new ChatMessageSender();

        mainComponent = new CardDrawer();
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
        print();
    }

    private void nextState() {

    }
}
