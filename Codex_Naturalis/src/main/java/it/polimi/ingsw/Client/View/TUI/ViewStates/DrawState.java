package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.*;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DrawState extends GameState {
    List<Component> passiveComponents;

    private final Logger logger;

    public DrawState(ClientModel model) {
        super(model);
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new ChatNotification(this));
        passiveComponents.add(new SharedObjectiveView(this));
        passiveComponents.add(new SecretObjectiveView(this));
        passiveComponents.add(new ScoreBoardView(this));
        passiveComponents.add(new CardMapView(this));
        passiveComponents.add(new TurnShower(this));

        mainComponent = new CardDrawer(this);
        if(Game.getInstance().isSetupFinished())
            addSecondaryComponent(new Zoomer(this));

        print();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        if(!Game.getInstance().isLastRoundFlag())
            TextUI.displayGameTitle();
        else
            TextUI.displayLastRound();

        for (Component component : passiveComponents) {
            component.print();
        }

        super.print();
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }
}
