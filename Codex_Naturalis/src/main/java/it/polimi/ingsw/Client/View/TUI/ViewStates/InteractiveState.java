package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponentReturns;

import java.util.logging.Logger;

/**
 * Abstract class representing a state with interactive components.
 */
public abstract class InteractiveState extends ViewState {
    private final InteractiveComponent mainComponent;
    boolean attemptToExitMainComponent;
    private final Logger logger;

    /**
     * Constructs an InteractiveState with the specified main component.
     *
     * @param mainComponent the main interactive component
     */
    public InteractiveState(InteractiveComponent mainComponent) {
        logger = Logger.getLogger(InteractiveState.class.getName());
        this.mainComponent = mainComponent;
        attemptToExitMainComponent = false;
    }

    /**
     * Gets the main interactive component.
     *
     * @return the main interactive component
     */
    public InteractiveComponent getMainComponent() {
        return mainComponent;
    }

    /**
     * Prints the state. Displays a message if attempting to exit the main component is not allowed.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            if (attemptToExitMainComponent) {
                attemptToExitMainComponent = false;
                System.out.println("\nYou can't go further back than this, please follow the instructions on screen.");
            }
        }
    }

    /**
     * Handles input from the user and processes it using the main component.
     *
     * @param input the user input
     * @return always returns true
     */
    @Override
    public boolean handleInput(String input) {
        logger.info("Handling input with main component: " + mainComponent.getClass().getSimpleName());
        InteractiveComponentReturns returnValue = mainComponent.handleInput(input);
        if (returnValue.equals(InteractiveComponentReturns.QUIT))
            attemptToExitMainComponent = true;

        if (ClientModel.getInstance().getView().equals(this))
            ClientModel.getInstance().printView();

        return true;
    }

    /**
     * Refreshes the observables for the main component.
     */
    @Override
    public void refreshObservables() {
        mainComponent.refreshObserved();
    }

    /**
     * Determines the next state based on the current state of the model.
     *
     * @return true if the state has been changed, false otherwise
     */
    boolean nextState() {
        synchronized (nextStateLock) {
            logger.info("Evaluating next state in InteractiveState abstract class");
            if (ClientModel.getInstance().getView().equals(this)) {
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
}