package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LogInSignUp;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

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
            model.unsubscribe(this);
            sleepOnObservables();
            model.setView(new LobbySelectionState(model));

            model.getView().print();
            return true;
        }
        return false;
    }
}
