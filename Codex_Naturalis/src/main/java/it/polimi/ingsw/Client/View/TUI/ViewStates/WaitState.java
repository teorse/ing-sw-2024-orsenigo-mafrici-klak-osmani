package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.States.*;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.WaitInteractor;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WaitState {
    List<Component> passiveComponenets;
    List<Component> postSetupComponents;
    InteractiveComponent mainComponent;

    InteractiveComponent activeComponent;

    private final Logger logger;


    public WaitState() {
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponenets = new ArrayList<>();
        passiveComponenets.add(new ChatNotification());
        passiveComponenets.add(new WaitTypeView());

        postSetupComponents = new ArrayList<>();
        postSetupComponents.add(new SharedObjectiveView());
        postSetupComponents.add(new SecretObjectiveView());
        postSetupComponents.add(new PointTableView());
        postSetupComponents.add(new CardMapView());
        postSetupComponents.add(new TurnShower());

        mainComponent = new WaitInteractor();
    }

    public void print() {
        //todo add class game and logic to switch between game title and last round
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        //todo add display of all available commands

        if (ClientModel.getInstance().isSetUpFinished()) {
            for (Component component : postSetupComponents) {
                component.print();
            }
        }
        for (Component component : passiveComponenets) {
            component.print();
        }
        mainComponent.print();
    }

    public void handleInput(String input) {
        //todo implement quit handling
        //todo implement chat handling

        mainComponent.handleInput(input);
    }

    public void update(){
        //todo add inside components the ability to comunicate to the state when it's time to update
        print();
    }

    private void nextState(){
        logger.info("Choosing next state");
        logger.fine("Current value of myPR State: " + model.getMyPlayerGameState());

        if (model.isGameOver()) {
            model.setClientState(new GameOverState(model));
        } else if (model.getMyPlayerGameState() == PlayerStates.WAIT) {
            logger.fine("Staying in Wait state");
            print();
        } else if (model.getMyPlayerGameState() == PlayerStates.DRAW) {
            logger.fine("Entering GameDrawState");
            model.setClientState(new GameDrawState(model));
        } else if (model.getMyPlayerGameState() == PlayerStates.PICK_OBJECTIVE) {
            logger.fine("Entering GamePickObjectiveState");
            model.setClientState(new GamePickObjectiveState(model));
        } else if (model.getMyPlayerGameState() == PlayerStates.PLACE) {
            logger.fine("Entering GamePlaceState");
            model.setClientState(new GamePlaceState(model));
        } else
            print();
    }

}
