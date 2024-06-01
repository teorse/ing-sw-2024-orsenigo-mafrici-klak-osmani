package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.States.GamePlaceState;
import it.polimi.ingsw.Client.Model.States.GameWaitState;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.PickSecretObjective;
import it.polimi.ingsw.Client.View.TUI.Components.ObjectiveCandidatesView;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;

public class GamePickObjectiveState extends LobbyStates {
    Component component;
    InteractiveComponent mainComponent;

    PlayerStates myPlayerGameState;




    public GamePickObjectiveState(ClientModel2 model) {
        super(model);
        mainComponent = new PickSecretObjective();
        component = new ObjectiveCandidatesView(this);
        myPlayerGameState = MyPlayer.getInstance().getMyPlayerGameState();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        mainComponent.print();
    }

    @Override
    public void handleInput(String input) {
        if (mainComponent.handleInput(input) == InteractiveComponentReturns.INCOMPLETE) {
            mainComponent.print();
            System.out.println("Not a valid input. Try again!");
        }
    }

    @Override
    public void update() {
        nextState();
    }

     boolean nextState() {
        if(model.isGameOver())
            model.setView(new GameOverState(model));
        else if (myPlayerGameState == PlayerStates.WAIT) {
            model.setView(new WaitState(model));
        } else if (myPlayerGameState == PlayerStates.PLACE) {
            model.setView(new PlaceState(model));
        }
        else
            print();
    }
}
