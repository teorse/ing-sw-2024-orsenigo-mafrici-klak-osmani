package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.MyPlayer;

public abstract class GameState extends LobbyStates{
    private final ClientModel model;

    public GameState(ClientModel model) {
        super(model);
    }

    public GameState(InteractiveComponent mainComponent) {
        super(mainComponent);
        model = ClientModel.getInstance();
    }

    boolean nextState(){
        if(MyPlayer.getInstance().isNewState() && !ClientModel.getInstance().isGameOver()){
            model.unsubscribe(this);
            sleepOnObservables();
            
            switch (MyPlayer.getInstance().getMyPlayerGameState()) {
                case PLACE -> {
                    if (!Game.getInstance().isSetupFinished())
                        model.setView(new StarterPlaceState(model));
                    else
                        model.setView(new PlaceState(model));
                }
                case DRAW -> model.setView(new DrawState(model));
                case PICK_OBJECTIVE -> model.setView(new GamePickObjectiveState(model));
                case WAIT -> model.setView(new WaitState(model));
            }

            model.getView().print();
            return true;

        } else if (ClientModel.getInstance().isGameOver()) {
            model.unsubscribe(this);
            sleepOnObservables();
            model.setView(new GameOverState(model));

            model.getView().print();
            return true;
        } else
            return false;
    }
}
