package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.Model.MyPlayer;

public abstract class GameState extends LobbyStates{

    public GameState(ClientModel2 model) {
        super(model);
    }

    boolean nextState(){
        if(MyPlayer.getInstance().isNewState() && !ClientModel2.getInstance().isGameOver()){
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
            return true;
        } else if (ClientModel2.getInstance().isGameOver()) {
            model.unsubscribe(this);
            sleepOnObservables();
            model.setView(new GameOverState(model));
            return true;
        } else
            return false;
    }
}
