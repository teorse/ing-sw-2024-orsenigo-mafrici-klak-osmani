package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.Zoomer;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WaitState extends GameState {
    List<Component> passiveComponents;
    List<Component> postSetupComponents;

    private final Logger logger;


    public WaitState(ClientModel2 model) {
        super(model);
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification(this));
        passiveComponents.add(new WaitTypeView(this));

        postSetupComponents = new ArrayList<>();
        postSetupComponents.add(new SharedObjectiveView(this));
        postSetupComponents.add(new SecretObjectiveView(this));
        postSetupComponents.add(new ScoreBoardView(this));
        postSetupComponents.add(new CardMapView(this));
        postSetupComponents.add(new TurnShower(this));

        if(Game.getInstance().isSetupFinished())
            mainComponent = new Zoomer(this);
        else
            mainComponent = null;
    }

    @Override
    public void print() {
        //todo add class game and logic to switch between game title and last round
        TextUI.clearCMD();
        if(!Game.getInstance().isLastRoundFlag())
            TextUI.displayGameTitle();
        else
            TextUI.displayLastRound();

        if (Game.getInstance().isSetupFinished()){
            for (Component component : postSetupComponents) {
                component.print();
            }
        }
        for (Component component : passiveComponents) {
            component.print();
        }
        if(mainComponent != null)
            super.print();
    }

    @Override
    public boolean handleInput(String input) {
        if(mainComponent != null)
            super.handleInput(input);
        return true;
    }

    @Override
    public void update(){
        if(!nextState())
            print();
    }
}
