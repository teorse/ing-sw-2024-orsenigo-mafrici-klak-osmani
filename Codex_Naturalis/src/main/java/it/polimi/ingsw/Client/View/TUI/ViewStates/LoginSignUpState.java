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

        if (getMainComponent().getInputCounter() > 0)
            System.out.println("\nIf you want to go back at the previous choice type: /back");

        super.print();

        getMainComponent().print();
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }

    boolean nextState(){
        ClientModel model = ClientModel.getInstance();

        if(super.nextState())
            return true;

        if(model.isLoggedIn()){
            RefreshManager.getInstance().resetObservables();
            model.setView(new LobbySelectionState());

            model.getView().print();
            return true;
        }
        return false;
    }
}
