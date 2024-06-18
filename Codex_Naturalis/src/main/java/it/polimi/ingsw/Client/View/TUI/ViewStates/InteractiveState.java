package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;

public abstract class InteractiveState extends ViewState {
    InteractiveComponent mainComponent;
    boolean attemptToExitMainComponent;
    boolean mainComponentCompleted;

    public InteractiveState(ClientModel model) {
        super(model);
        attemptToExitMainComponent = false;
        mainComponentCompleted = false;
    }

    @Override
    public void print() {
        if(attemptToExitMainComponent) {
            attemptToExitMainComponent = false;
            System.out.println("You can't go further back than this, please follow the instructions on screen.");
        }
    }

    @Override
    public boolean handleInput(String input) {
        if(!mainComponentCompleted) {
            InteractiveComponentReturns returnValue = mainComponent.handleInput(input);
            if (returnValue.equals(InteractiveComponentReturns.QUIT))
                attemptToExitMainComponent = true;
            else if (returnValue.equals(InteractiveComponentReturns.COMPLETE)) {
                mainComponentCompleted = true;
            }
            if (model.getView().equals(this))
                print();
        }

        return true;
    }
}
