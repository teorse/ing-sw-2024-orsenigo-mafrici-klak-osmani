package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.GameOverView;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;

public class GameOverState extends ViewState {

    private final LiveComponent gameOverView;
    private boolean exitGameOver;

    public GameOverState() {
        super();
        gameOverView = new GameOverView();
        exitGameOver = false;
    }

    @Override
    public void print() {
        gameOverView.print();
    }

    @Override
    public boolean handleInput(String input) {exitGameOver = true;
       update();
       return true;
    }

    @Override
    public void refreshObservables() {
        gameOverView.refreshObserved();
    }

    @Override
    public void update() {
        if(exitGameOver) {
            gameOverView.cleanObserved();
            RefreshManager.getInstance().resetObservables();

            ClientModel.getInstance().setView(new LobbyJoinedState());
            ClientModel.getInstance().getView().print();
        }
        else
            print();
    }
}
