package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.LogInSignUp;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

/**
 * Represents the state where the player logs in or signs up.
 */
public class LoginSignUpState extends InteractiveState {
    private final Logger logger;

    /**
     * Constructs a LoginSignUpState with the LogInSignUp component as the main component.
     */
    public LoginSignUpState() {
        super(new LogInSignUp());
        logger = Logger.getLogger(LoginSignUpState.class.getName());
    }

    /**
     * Prints the current state, including the login/signup component.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            TextUI.displayGameTitle();

            super.print();

            getMainComponent().print();
        }
    }

    /**
     * Updates the state and checks if there is a next state to transition to.
     */
    @Override
    public void update() {
        logger.fine("Updating in LoginSignUpState");
        if (!nextState())
            ClientModel.getInstance().printView();
        logger.fine("Finished updating in LoginSignUpState");
    }

    /**
     * Determines the next state based on the current conditions.
     * @return true if a next state is found and transitioned to, false otherwise.
     */
    boolean nextState() {
        ClientModel model = ClientModel.getInstance();

        synchronized (nextStateLock) {
            if (model.getView().equals(this)) {
                if (!ClientModel.getInstance().isConnected()) {
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