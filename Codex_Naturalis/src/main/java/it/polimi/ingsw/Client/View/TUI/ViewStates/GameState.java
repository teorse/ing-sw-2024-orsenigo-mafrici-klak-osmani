package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.MyPlayer;

public abstract class GameState extends LobbyStates{
    private final ClientModel model;

    public GameState(InteractiveComponent mainComponent, List<InteractiveComponent> secondaryComponents) {
        super(mainComponent, secondaryComponents);
        model = ClientModel.getInstance();
    }

    public GameState(InteractiveComponent mainComponent) {
        super(mainComponent);
        model = ClientModel.getInstance();
    }

    @Override
    boolean nextState(){
        if(super.nextState())
            return true;

        if(ClientModel.getInstance().isGameOver()){
            RefreshManager.getInstance().resetObservables();
            ClientModel.getInstance().setView(new GameOverState());
            ClientModel.getInstance().getView().print();
            return true;
        }
        return false;
    }
}
