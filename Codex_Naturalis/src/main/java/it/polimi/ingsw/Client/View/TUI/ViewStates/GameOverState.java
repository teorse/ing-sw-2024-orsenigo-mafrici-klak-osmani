package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.GameOverView;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

/**
 * Represents the state of the game when it is over.
 */
public class GameOverState extends ViewState {

    private final Logger logger;
    private final LiveComponent gameOverView;
    private boolean exitGameOver;

    /**
     * Constructs the GameOverState and initializes its components.
     */
    public GameOverState() {
        super();
        logger = Logger.getLogger(GameOverState.class.getName());
        gameOverView = new GameOverView();
        exitGameOver = false;
    }

    /**
     * Prints the game overview to the console.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();

            gameOverView.print();
        }
    }

    /**
     * Handles input from the user. In this state, any input will exit the game over state.
     *
     * @param input the input from the user.
     * @return true if the input was handled.
     */
    @Override
    public boolean handleInput(String input) {
        exitGameOver = true;
        ClientModel.getInstance().resetGame();
        update();
        return true;
    }

    /**
     * Refreshes the observables for the game overview.
     */
    @Override
    public void refreshObservables() {
        gameOverView.refreshObserved();
    }

    /**
     * Updates the state, checking for transitions to the next state. If no transition occurs, it reprints the current view.
     */
    @Override
    public void update() {
        logger.fine("Updating in GameOverState");
        if (!nextState()) {
            ClientModel.getInstance().printView();
        }
        logger.fine("finished updating in GameOverState");
    }

    /**
     * Determines the next state based on the current conditions.
     *
     * @return true if the state was changed, false otherwise.
     */
    private boolean nextState() {
        synchronized (nextStateLock) {
            if (ClientModel.getInstance().getView().equals(this)) {
                if (exitGameOver) {
                    gameOverView.cleanObserved();
                    RefreshManager.getInstance().resetObservables();

                    ClientModel.getInstance().setView(new LobbyJoinedState());
                    ClientModel.getInstance().printView();

                    return true;
                }
                return false;
            }
            return true;
        }
    }
}