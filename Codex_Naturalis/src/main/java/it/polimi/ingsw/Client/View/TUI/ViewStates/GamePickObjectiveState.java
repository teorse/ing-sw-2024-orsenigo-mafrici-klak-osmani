package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.PickSecretObjective;
import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.ObjectiveCandidatesView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

public class GamePickObjectiveState extends GameState {
    private final LiveComponent objectiveCandidates;
    private final Logger logger;


    public GamePickObjectiveState() {
        super(new PickSecretObjective());
        logger = Logger.getLogger(GamePickObjectiveState.class.getName());

        objectiveCandidates = new ObjectiveCandidatesView();
        refreshObservables();
    }

    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            if(!Game.getInstance().isLastRoundFlag())
                TextUI.displayGameTitle();
            else
                TextUI.displayLastRound();

            objectiveCandidates.print();
            getActiveComponent().print();
            super.print();
        }
    }

    @Override
    public void refreshObservables() {
        super.refreshObservables();
        objectiveCandidates.refreshObserved();
    }

    @Override
    public void update() {
        logger.fine("Updating in GamePickObjectiveState");
        if(!nextState())
            ClientModel.getInstance().printView();
        logger.fine("finished updating in GamePickObjectiveState");
    }
}
