package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ServerConnectionWizard;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class ConnectionState extends ViewState {
    InteractiveComponent mainComponent;
    boolean attemptedToQuitMainComponent;

    public ConnectionState(ClientModel model){
        super(model);
        attemptedToQuitMainComponent = false;
        mainComponent = new ServerConnectionWizard(this);
        print();
    }


    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        if(attemptedToQuitMainComponent){
            attemptedToQuitMainComponent = false;
            System.out.println("You can't go further back than this, please follow the instructions on screen.");
        }
        mainComponent.print();
    }

    @Override
    public boolean handleInput(String input) {
        InteractiveComponentReturns returnValue = mainComponent.handleInput(input);
        if(returnValue.equals(InteractiveComponentReturns.QUIT))
            attemptedToQuitMainComponent = true;
        print();

        return true;
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }

    private boolean nextState(){
        if (model.isConnected()) {
            model.unsubscribe(this);
            model.setView(new LoginSignUpState(model));
            return true;
        } else
            return false;
    }
}
