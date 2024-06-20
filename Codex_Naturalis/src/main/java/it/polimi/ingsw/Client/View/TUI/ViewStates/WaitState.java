package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.Zoomer;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WaitState extends GameState {
    List<LiveComponent> passiveComponents;
    List<LiveComponent> postSetupComponents;

    private final Logger logger;

    public WaitState() {
        super(new Zoomer());
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification());
        passiveComponents.add(new WaitTypeView());

        postSetupComponents = new ArrayList<>();
        postSetupComponents.add(new SharedObjectiveView());
        postSetupComponents.add(new SecretObjectiveView());
        postSetupComponents.add(new ScoreBoardView());
        postSetupComponents.add(new CardMapView());
        postSetupComponents.add(new TurnShower());
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        if(!Game.getInstance().isLastRoundFlag())
            TextUI.displayGameTitle();
        else
            TextUI.displayLastRound();

        System.out.println("\nTo display the available commands type /help or /h");

        if (Game.getInstance().isSetupFinished()){
            for (Component component : postSetupComponents) {
                component.print();
            }
        }
        for (Component component : passiveComponents) {
            component.print();
        }

        getActiveComponent().print();
        super.print();
    }

    @Override
    public void refreshObservables() {
        for(LiveComponent component : passiveComponents){
            component.refreshObserved();
        }
        for(LiveComponent component : postSetupComponents){
            component.refreshObserved();
        }
    }

    @Override
    public void update(){
        if(!nextState())
            print();
    }
}
