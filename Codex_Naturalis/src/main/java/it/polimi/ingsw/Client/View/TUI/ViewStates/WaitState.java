package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
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
        logger.info("Initializing wait State");

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification());
        passiveComponents.add(new WaitTypeView());

        postSetupComponents = new ArrayList<>();
        postSetupComponents.add(new SharedObjectiveView());
        postSetupComponents.add(new SecretObjectiveView());
        postSetupComponents.add(new ScoreBoardView());
        postSetupComponents.add(new CardMapView());
        postSetupComponents.add(new TurnShower());

        refreshObservables();
    }

    @Override
    public synchronized void print() {
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

        getActiveComponent().print();
        super.print();
    }

    @Override
    public void refreshObservables() {
        super.refreshObservables();
        for(LiveComponent component : passiveComponents){
            component.refreshObserved();
        }
        for(LiveComponent component : postSetupComponents){
            component.refreshObserved();
        }
    }

    @Override
    public synchronized void update(){
        if(!nextState())
            ClientModel.getInstance().printView();
    }
}
