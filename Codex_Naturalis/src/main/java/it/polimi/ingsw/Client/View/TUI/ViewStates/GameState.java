package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;

import java.util.List;
import java.util.logging.Logger;

public abstract class GameState extends LobbyStates{
    //TODO same attribute as the father class
    private final ClientModel model;
    private final Logger logger;

    public GameState(InteractiveComponent mainComponent, List<InteractiveComponent> secondaryComponents) {
        super(mainComponent, secondaryComponents);
        logger = Logger.getLogger(GameState.class.getName());
        logger.info("Initializing GameState abstract class");

        model = ClientModel.getInstance();
    }

    public GameState(InteractiveComponent mainComponent) {
        super(mainComponent);
        logger = Logger.getLogger(GameState.class.getName());
        logger.info("Initializing GameState abstract class");

        model = ClientModel.getInstance();
    }

    @Override
    public void refreshObservables(){
        super.refreshObservables();
        RefreshManager.getInstance().addObserved(MyPlayer.getInstance());
    }

    @Override
    boolean nextState(){
        if(super.nextState())
            return true;

        logger.info("Evaluating next state in GameState");

        if(ClientModel.getInstance().isGameOver()){
            logger.fine("The game is over, setting view state to GameOvrState");
            RefreshManager.getInstance().resetObservables();
            ClientModel.getInstance().setView(new GameOverState());
            ClientModel.getInstance().getView().print();
            return true;
        }
        logger.fine("No elegible state found, returning false");
        return false;
    }
}
