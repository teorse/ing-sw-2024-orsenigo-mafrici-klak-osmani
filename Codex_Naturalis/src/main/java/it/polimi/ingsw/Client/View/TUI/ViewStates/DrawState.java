package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.*;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DrawState extends GameState {
    List<LiveComponent> passiveComponents;

    private final Logger logger;

    public DrawState() {
        super(new CardPlacer(), new ArrayList<>(){{add(new Zoomer());}});
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification());
        passiveComponents.add(new SharedObjectiveView());
        passiveComponents.add(new SecretObjectiveView());
        passiveComponents.add(new ScoreBoardView());
        passiveComponents.add(new CardMapView());
        passiveComponents.add(new TurnShower());
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        if(!Game.getInstance().isLastRoundFlag())
            TextUI.displayGameTitle();
        else
            TextUI.displayLastRound();

        System.out.println("\nTo display the available commands type /help or /h");

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
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }
}
