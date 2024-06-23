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
    public synchronized void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        super.print();

        getMainComponent().print();
    }

    @Override
    public synchronized void update() {
        if(!nextState())
            ClientModel.getInstance().printView();
    }

    synchronized boolean nextState(){
        ClientModel model = ClientModel.getInstance();

        if(model.getView().equals(this)) {
            if(!ClientModel.getInstance().isConnected()){
                RefreshManager.getInstance().resetObservables();
                model.setView(new ConnectionState());

                model.printView();
                return true;
            }

            if (model.isLoggedIn()) {
                RefreshManager.getInstance().resetObservables();
                model.setView(new LobbySelectionState());

                model.printView();
                return true;
            }
            return false;
        }
        return true;
    }
}
