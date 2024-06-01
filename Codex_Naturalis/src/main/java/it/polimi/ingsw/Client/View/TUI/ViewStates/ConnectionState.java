package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ServerConnectionWizard;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class ConnectionState extends ViewState {
    InteractiveComponent mainComponent;

    public ConnectionState(ClientModel2 model){
        super(model);
        mainComponent = new ServerConnectionWizard();
        print();
    }


    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        mainComponent.print();
    }

    @Override
    public void handleInput(String input) {
        mainComponent.handleInput(input);
    }

    @Override
    public void update() {
        //todo add logic to check if the correct boolean have updated and if so go to next State
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
