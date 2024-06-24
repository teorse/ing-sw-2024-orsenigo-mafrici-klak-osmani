package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ServerConnectionWizard;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.logging.Logger;

/**
 * Represents the connection state where the client attempts to connect to the server.
 */
public class ConnectionState extends InteractiveState {
    private final Logger logger;

    /**
     * Constructs a ConnectionState with a server connection wizard as the main interactive component.
     */
    public ConnectionState() {
        super(new ServerConnectionWizard());
        logger = Logger.getLogger(ConnectionState.class.getName());
        logger.info("Initializing Connection State view State");
    }

    /**
     * Prints the current state to the console, including clearing the command line and displaying the game title.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            logger.info("Printing viewState");
            TextUI.clearCMD();
            TextUI.displayGameTitle();

            super.print();

            getMainComponent().print();
        }
    }

    /**
     * Updates the state. If no next state is available, it reprints the current view.
     */
    @Override
    public void update() {
        logger.info("Updating ConnectionState");
        if (!nextState()) {
            ClientModel.getInstance().printView();
        }
        logger.fine("finished updating in ConnectionState");
    }

    /**
     * Checks and transitions to the next state if certain conditions are met, such as a successful connection.
     * @return true if the state transitions to a new state, false otherwise
     */
    @Override
    boolean nextState() {
        synchronized (nextStateLock) {
            logger.info("Checking evaluating next state in ConnectionState");

            if (ClientModel.getInstance().getView().equals(this)) {
                ClientModel model = ClientModel.getInstance();

                logger.fine("No next state was found in the superClass, continuing in ConnectionState to look for next state.");

                if (model.isConnected()) {
                    RefreshManager.getInstance().resetObservables();
                    model.setView(new LoginSignUpState());
                    ClientModel.getInstance().printView();
                    logger.fine("next state chosen is LoginSignUpState");
                    return true;
                } else {
                    logger.fine("No next state was found in the base class, returning false.");
                    return false;
                }
            }
            return true;
        }
    }
}