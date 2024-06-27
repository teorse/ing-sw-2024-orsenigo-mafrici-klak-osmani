package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Network.ClientConnectorRMI;
import it.polimi.ingsw.Client.Network.ClientConnectorSocket;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.Utils.Utilities;

import java.net.SocketTimeoutException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * The `ServerConnectionWizard` component facilitates the process of establishing a connection
 * to the game server. It guides the user through selecting the connection type (RMI or Socket)
 * and entering the server's IP address. It handles input validation, connection attempts, and
 * displays appropriate messages based on the connection status.
 */
public class ServerConnectionWizard extends InteractiveComponent {

    private final Logger logger;
    private int choice;
    private boolean malformedIp;
    private boolean connectionTimedOut;
    private boolean remoteException;

    public ServerConnectionWizard() {
        super(1);
        logger = Logger.getLogger(ServerConnectionWizard.class.getName());
        logger.info("Initializing Server Connection wizard");
        malformedIp = false;
        connectionTimedOut = false;
        remoteException = false;

        refreshObserved();

        logger.info("Server Connection wizard initialized");
    }

    /**
     * Handles user input for establishing a connection to the server.
     * This method processes the input through two stages: selecting the connection type (RMI or Socket),
     * and entering the server's IP address. It validates the input at each stage and attempts to connect
     * to the server using the selected connection type.
     *
     * @param input the user input string to be processed
     * @return the state of the input handling process as an InteractiveComponentReturns enum
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        // Reset connection error flags
        connectionTimedOut = false;
        remoteException = false;
        malformedIp = false;

        // Process input through superclass method
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        int inputCounter = getInputCounter();

        // Handle input for selecting connection type (RMI or Socket)
        if (inputCounter == 0) {
            if (InputValidator.validBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                incrementInputCounter();
            }
            return InteractiveComponentReturns.INCOMPLETE;

            // Handle input for entering server IP address
        } else if (inputCounter == 1) {
            if (input.isEmpty())
                input = "127.0.0.1";
            else if (!InputValidator.isValidIPAddress(input)){
                malformedIp = true;
                return InteractiveComponentReturns.INCOMPLETE;
            }

            // Attempt to connect using RMI
            if (choice == 1) {
                connectionTimedOut = false;
                remoteException = false;
                try {
                    ClientModel model = ClientModel.getInstance();
                    System.out.println("Attempting connection to server");
                    model.setClientConnector(new ClientConnectorRMI(input, new ClientController(model), model));
                    incrementInputCounter();
                    return InteractiveComponentReturns.COMPLETE;
                } catch (ConnectException connectException) {
                    connectionTimedOut = true;
                    String stackTrace = Utilities.StackTraceToString(connectException);
                    logger.warning("Connection timed out while connecting to RMI server: " + input + "\n" +
                            "Stacktrace:\n" + stackTrace);
                } catch (RemoteException e) {
                    String stackTrace = Utilities.StackTraceToString(e);
                    logger.warning("RemoteException was thrown while creating ClientConnectorRMI\nStacktrace:\n" + stackTrace);
                    remoteException = true;
                }

                // Attempt to connect using Socket
            } else {
                try {
                    ClientModel model = ClientModel.getInstance();
                    System.out.println("Attempting connection to server");
                    model.setClientConnector(new ClientConnectorSocket(input, new ClientController(model), model));
                    incrementInputCounter();
                    return InteractiveComponentReturns.COMPLETE;
                } catch (SocketTimeoutException socketTimeoutException) {
                    connectionTimedOut = true;
                }

            }
            return InteractiveComponentReturns.INCOMPLETE;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }


    /**
     *{@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "connect";
    }

    /**
     * Provides a description of this component.
     *
     * @return an empty string (no specific description provided)
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * Prints messages and prompts based on the current state of the connection process.
     * Displays instructions for choosing between RMI or Socket connection and entering the server's IP address.
     * Also handles displaying connection error messages if the connection attempt fails.
     */
    @Override
    public void print() {
        logger.info("Printing ServerConnectionWizard");
        super.print();

        if(connectionTimedOut) {
            System.out.println("\nCould not connect to the Server.\nTry with another server ip.");
        }

        int inputCounter = getInputCounter();
        if (inputCounter == 0) {
            logger.fine("printing input counter = 0");
            System.out.println("\n" +  """
                    Enter your choice:
                     1 - RMI
                     2 - Socket""");
        } else if (inputCounter == 1) {
            logger.fine("printing input counter = 1");

            if (remoteException) {
                System.out.println("\nAn error occurred while connecting to the server, please check the logs.");
            }
            else if (malformedIp) {
                System.out.println("\nThe provided IP is not correctly formatted, please type it again.");
                System.out.println("Enter the IP address of the server, leave empty for localhost: ");
            }
            else
                System.out.println("\nEnter the IP address of the server, leave empty for localhost: ");
        } else if (inputCounter == 2) {
            System.out.println("Waiting for serverResponse");
        }
    }




    /**
     * Cleans up any resources or observations associated with this component.
     * This method is empty because the `ServerConnectionWizard` component does not observe any resources.
     */
    //Method is empty because this component does not observe anything.
    @Override
    public void cleanObserved() {}

    /**
     * Refreshes any observations or resources associated with this component.
     * This method is empty because the `ServerConnectionWizard` component does not observe any resources.
     */
    @Override
    public void refreshObserved() {

    }
}
