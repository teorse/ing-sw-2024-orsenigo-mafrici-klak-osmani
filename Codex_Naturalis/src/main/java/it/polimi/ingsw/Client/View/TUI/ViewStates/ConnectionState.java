package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ServerConnectionWizard;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

public class ConnectionState extends InteractiveState {
    private final Logger logger;

    public ConnectionState(){
        super(new ServerConnectionWizard());
        logger = Logger.getLogger(ConnectionState.class.getName());
        logger.info("Initializing Connection State view State");
    }


    @Override
    public synchronized void print() {
        logger.info("Printing viewState");
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        super.print();

        getMainComponent().print();
    }

    @Override
    public synchronized void update() {
        logger.info("Updating viewState");
        if(!nextState())
            ClientModel.getInstance().printView();
    }

    @Override
    synchronized boolean nextState(){
        logger.info("Checking evaluating next state in ConnectionState");

        if(ClientModel.getInstance().getView().equals(this)) {
            ClientModel model = ClientModel.getInstance();

            logger.fine("No next state was found in the superClass, continuing in ConnectionState to look for next state.");

            if (model.isConnected()) {
                RefreshManager.getInstance().resetObservables();
                model.setView(new LoginSignUpState());
                ClientModel.getInstance().printView();
                logger.fine("next state chosen is LoginSignUpState");
                return true;
            } else {
                logger.fine("No next state was found in the base class, returing false.");
                return false;
            }
        }
        return true;
    }
}
