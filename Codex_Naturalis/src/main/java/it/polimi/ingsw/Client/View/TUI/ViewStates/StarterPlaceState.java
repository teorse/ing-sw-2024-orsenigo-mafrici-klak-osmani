package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.CardsHeldView;
import it.polimi.ingsw.Client.View.TUI.Components.Component;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.CardStarterChoice;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class StarterPlaceState extends GameState {
    Component component;
    InteractiveComponent mainComponent;

    boolean attemptToQuitMainComponent;
    boolean waitingForServerResponse;



    public StarterPlaceState(ClientModel model) {
        super(model);
        component = new CardsHeldView(this);
        mainComponent = new CardStarterChoice(this);

        attemptToQuitMainComponent = false;

        print();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        component.print();
        if(!waitingForServerResponse) {
            if (attemptToQuitMainComponent)
                System.out.println("You can't exit the main component");
            mainComponent.print();
        }
        else
            System.out.println("Waiting for server response");
    }

    @Override
    public boolean handleInput(String input) {
        if(super.handleInput(input))
            return true;

        if(!waitingForServerResponse) {
            InteractiveComponentReturns result = mainComponent.handleInput(input);
            if (result.equals(InteractiveComponentReturns.QUIT))
                attemptToQuitMainComponent = true;
            else if (result.equals(InteractiveComponentReturns.COMPLETE)) {
                waitingForServerResponse = true;
            }
        }

        return true;
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }
}
