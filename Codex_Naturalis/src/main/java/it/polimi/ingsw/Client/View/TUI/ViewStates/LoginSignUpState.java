package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LogInSignUp;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class LoginSignUpState extends InteractiveState {

    public LoginSignUpState() {
        super(new LogInSignUp());
    }

    @Override
    public void print() {
            TextUI.clearCMD();
            TextUI.displayGameTitle();

            super.print();

            mainComponent.print();
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }

    private boolean nextState(){
        if(model.isLoggedIn()){
            RefreshManager.getInstance().resetObservables();
            model.setView(new LobbySelectionState());

            model.getView().print();
            return true;
        }
        return false;
    }
}
