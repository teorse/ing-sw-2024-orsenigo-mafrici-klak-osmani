package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.GameOver;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;

import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract class representing the state of the game within the lobby.
 */
public abstract class GameState extends LobbyStates {
    private final ClientModel model;
    private final Logger logger;

    /**
     * Constructs a GameState with the specified main component and secondary components.
     *
     * @param mainComponent the main interactive component
     * @param secondaryComponents the list of secondary interactive components
     */
    public GameState(InteractiveComponent mainComponent, List<InteractiveComponent> secondaryComponents) {
        super(mainComponent, secondaryComponents);
        logger = Logger.getLogger(GameState.class.getName());
        logger.info("Initializing GameState abstract class");

        model = ClientModel.getInstance();
    }

    /**
     * Constructs a GameState with the specified main component.
     *
     * @param mainComponent the main interactive component
     */
    public GameState(InteractiveComponent mainComponent) {
        super(mainComponent);
        logger = Logger.getLogger(GameState.class.getName());
        logger.info("Initializing GameState abstract class");

        model = ClientModel.getInstance();
    }

    /**
     * Refreshes the observables for the game state.
     */
    @Override
    public void refreshObservables() {
        super.refreshObservables();
        RefreshManager.getInstance().addObserved(MyPlayer.getInstance());
        RefreshManager.getInstance().addObserved(ClientModel.getInstance());
        RefreshManager.getInstance().addObserved(GameOver.getInstance());

    }

    /**
     * Determines the next state based on the current state of the model.
     *
     * @return true if the state has been changed, false otherwise
     */
    @Override
    boolean nextState() {
        synchronized (nextStateLock) {
            if (model.getView().equals(this)) {
                if (super.nextState())
                    return true;

                logger.info("Evaluating next state in GameState");

                if (ClientModel.getInstance().isGameOver()) {
                    logger.fine("The game is over, setting view state to GameOverState");
                    RefreshManager.getInstance().resetObservables();
                    ClientModel.getInstance().setView(new GameOverState());
                    ClientModel.getInstance().printView();
                    return true;
                }
                logger.fine("No eligible state found, returning false");
                // Returns false because could not match conditions for next state
                return false;
            }
            logger.fine("State was already changed before this call, returning true");
            // Returns true because the initial if statement was false and therefore this state is already not the current state.
            return true;
        }
    }
}