package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;

import java.util.logging.Logger;

public abstract class InteractiveState extends ViewState {
    private final InteractiveComponent mainComponent;
    boolean attemptToExitMainComponent;
    private final Logger logger;

    public InteractiveState(InteractiveComponent mainComponent) {
        logger = Logger.getLogger(InteractiveState.class.getName());
        this.mainComponent = mainComponent;
        attemptToExitMainComponent = false;
    }

    public InteractiveComponent getMainComponent() {
        return mainComponent;
    }

    @Override
    public synchronized void print() {
        if(attemptToExitMainComponent) {
            attemptToExitMainComponent = false;
            System.out.println("\nYou can't go further back than this, please follow the instructions on screen.");
        }
    }

    @Override
    public boolean handleInput(String input) {
            InteractiveComponentReturns returnValue = mainComponent.handleInput(input);
            if (returnValue.equals(InteractiveComponentReturns.QUIT))
                attemptToExitMainComponent = true;

            if (ClientModel.getInstance().getView().equals(this))
                ClientModel.getInstance().printView();

        return true;
    }

    @Override
    public void refreshObservables(){
        mainComponent.refreshObserved();
    }

    synchronized boolean nextState(){
        logger.info("Evaluating next state in InteractiveState abstract class");
        if(ClientModel.getInstance().getView().equals(this)) {
            if (!ClientModel.getInstance().isConnected()) {
                RefreshManager.getInstance().resetObservables();
                ClientModel.getInstance().setView(new ConnectionState());
                ClientModel.getInstance().printView();

                logger.fine("next state chosen is ConnectionState");
                return true;
            } else if (!ClientModel.getInstance().isLoggedIn()) {
                RefreshManager.getInstance().resetObservables();
                ClientModel.getInstance().setView(new LoginSignUpState());
                ClientModel.getInstance().printView();

                logger.fine("next state chosen is LoginSignUpState");
                return true;
            }

            logger.fine("No eligible next state found, returning false");

            return false;
        }
        logger.fine("State was already changed before this call, returning true");
        return true;
    }
}
