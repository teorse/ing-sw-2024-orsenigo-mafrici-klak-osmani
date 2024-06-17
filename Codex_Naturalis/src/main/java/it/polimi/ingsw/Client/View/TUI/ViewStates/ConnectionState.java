package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ServerConnectionWizard;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

public class ConnectionState extends ViewState {
    private final InteractiveComponent mainComponent;
    private boolean attemptedToQuitMainComponent;
    private final Logger logger;

    public ConnectionState(ClientModel model){
        super(model);
        logger = Logger.getLogger(ConnectionState.class.getName());
        logger.info("Initializing Connection State view State");

        attemptedToQuitMainComponent = false;

        mainComponent = new ServerConnectionWizard(this);
    }


    @Override
    public void print() {
        logger.info("Printing viewState");
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        if(attemptedToQuitMainComponent){
            attemptedToQuitMainComponent = false;
            System.out.println("You can't go further back than this, please follow the instructions on screen.");
        }
        logger.fine("Attempting to print main component.");
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
            model.getView().print();
            return true;
        } else
            return false;
    }
}
