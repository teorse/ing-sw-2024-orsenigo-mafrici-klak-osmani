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
        logger.fine("Wait State initialized");
    }

    @Override
    public void print() {
        logger.info("Called print method in WaitState");

        synchronized (printLock) {
            TextUI.clearCMD();
            if (!Game.getInstance().isLastRoundFlag())
                TextUI.displayGameTitle();
            else
                TextUI.displayLastRound();

            if (Game.getInstance().isSetupFinished()) {
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
    }

    @Override
    public void refreshObservables() {
        logger.fine("Refreshing observables in Wait State");

        super.refreshObservables();
        for(LiveComponent component : passiveComponents){
            component.refreshObserved();
        }
        for(LiveComponent component : postSetupComponents){
            component.refreshObserved();
        }
    }

    @Override
    public void update(){
        logger.info("Called update method in WaitState");

        if(!nextState()) {
            logger.fine("No state was found to switch to from Wait State, proceeding to call model.print");
            ClientModel.getInstance().printView();
        }
        logger.fine("finished updating in WaitState");
    }
}
