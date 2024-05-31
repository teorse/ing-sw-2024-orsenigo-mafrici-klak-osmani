package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.States.*;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.WaitInteractor;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WaitState extends ViewState {
    List<Component> passiveComponenets;
    List<Component> postSetupComponents;
    InteractiveComponent mainComponent;

    PlayerStates myPlayerGameState;

    private final Logger logger;


    public WaitState(ClientModel2 model) {
        super(model);
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

        myPlayerGameState = MyPlayer.getInstance().getMyPlayerGameState();
    }

    @Override
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

    @Override
    public void handleInput(String input) {
        //todo implement quit handling
        //todo implement chat handling

        mainComponent.handleInput(input);
    }

    @Override
    public void update(){
        //todo add inside components the ability to comunicate to the state when it's time to update
        print();
    }

    private void nextState(){
        logger.info("Choosing next state");
        logger.fine("Current value of myPR State: " + myPlayerGameState);

        if (model.isGameOver()) {
            model.setView(new GameOverState(model));
        } else if (myPlayerGameState == PlayerStates.WAIT) {
            logger.fine("Staying in Wait state");
            print();
        } else if (myPlayerGameState == PlayerStates.DRAW) {
            logger.fine("Entering GameDrawState");
            model.setView(new DrawState(model));
        } else if (myPlayerGameState == PlayerStates.PICK_OBJECTIVE) {
            logger.fine("Entering GamePickObjectiveState");
            model.setView(new GamePickObjectiveState(model));
        } else if (myPlayerGameState == PlayerStates.PLACE) {
            logger.fine("Entering GamePlaceState");
            model.setView(new PlaceState(model));
        } else
            print();
    }
}
