package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.GameOverView;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

public class GameOverState extends ViewState {

    private final Logger logger;
    private final LiveComponent gameOverView;
    private boolean exitGameOver;

    public GameOverState() {
        super();
        logger = Logger.getLogger(GameOverState.class.getName());
        gameOverView = new GameOverView();
        exitGameOver = false;
    }

    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();

            gameOverView.print();
        }
    }

    @Override
    public boolean handleInput(String input) {
        exitGameOver = true;
        ClientModel.getInstance().resetGame();
        update();
        return true;
    }

    @Override
    public void refreshObservables() {
        gameOverView.refreshObserved();
    }

    @Override
    public void update() {
        logger.fine("Updating in GameOverState");
        if(!nextState())
            ClientModel.getInstance().printView();
        logger.fine("finished updating in GameOverState");
    }

    private boolean nextState(){
        synchronized (nextStateLock) {
            if(ClientModel.getInstance().getView().equals(this)) {
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
