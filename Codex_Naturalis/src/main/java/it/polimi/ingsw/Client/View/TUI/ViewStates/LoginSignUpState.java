package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LogInSignUp;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

public class LoginSignUpState extends InteractiveState {
    private final Logger logger;

    public LoginSignUpState() {
        super(new LogInSignUp());
        logger = Logger.getLogger(LoginSignUpState.class.getName());
    }

    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            TextUI.displayGameTitle();

            super.print();

            getMainComponent().print();
        }
    }

    @Override
    public void update() {
        logger.fine("Updating in LoginSignUpState");
        if(!nextState())
            ClientModel.getInstance().printView();
        logger.fine("finished updating in LoginSignUpState");
    }

    boolean nextState(){
        ClientModel model = ClientModel.getInstance();

        synchronized (nextStateLock) {
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
}
