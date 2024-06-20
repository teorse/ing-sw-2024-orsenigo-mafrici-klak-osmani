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

public class ServerConnectionWizard extends InteractiveComponent {

    private final Logger logger;
    private int choice;
    private boolean malformedIp;
    private boolean connectionTimedOut;
    private boolean remoteException;

    public ServerConnectionWizard() {
        super();
        logger = Logger.getLogger(ServerConnectionWizard.class.getName());
        logger.info("Initializing Server Connection wizard");
        malformedIp = false;
        connectionTimedOut = false;
        remoteException = false;

        refreshObserved();

        logger.info("Server Connection wizard initialized");
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        if (inputCounter == 0) {
            if (InputValidator.validBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                inputCounter++;
            }
            return InteractiveComponentReturns.INCOMPLETE;

        } else if (inputCounter == 1) {
            if (input.isEmpty())
                input = "127.0.0.1";
            else if (!InputValidator.isValidIPAddress(input)){
                malformedIp = true;
                return InteractiveComponentReturns.INCOMPLETE;
            }

            if (choice == 1) {
                connectionTimedOut = false;
                remoteException = false;
                try {
                    ClientModel model = ClientModel.getInstance();
                    System.out.println("Attempting connection to server");
                    model.setClientConnector(new ClientConnectorRMI(input, new ClientController(model), model));
                    inputCounter++;
                    return InteractiveComponentReturns.COMPLETE;
                }
                catch (ConnectException connectException) {
                    connectionTimedOut = true;
                    String stackTrace = Utilities.StackTraceToString(connectException);
                    logger.warning("Connection timed out while connecting to RMI server: " + input + "\n" +
                            "Stacktrace:\n" + stackTrace);
                }
                catch (RemoteException e) {
                    String stackTrace = Utilities.StackTraceToString(e);
                    logger.warning("RemoteException was thrown while creating ClientConnectorRMI\nStacktrace:\n" + stackTrace);
                    remoteException = true;
                }

            } else {
                try {
                    ClientModel model = ClientModel.getInstance();
                    System.out.println("Attempting connection to server");
                    model.setClientConnector(new ClientConnectorSocket(input, new ClientController(model), model));
                    inputCounter++;
                    return InteractiveComponentReturns.COMPLETE;
                } catch (SocketTimeoutException socketTimeoutException) {
                    connectionTimedOut = true;
                }

            }
            return InteractiveComponentReturns.INCOMPLETE;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }



    @Override
    public String getKeyword() {
        return "connect";
    }

    @Override
    public void print() {
        logger.info("Printing ServerConnectionWizard");

        if(connectionTimedOut) {
            connectionTimedOut = false;
            System.out.println("\nCould not connect to the Server.\nTry with another server ip.");
        }

        if (inputCounter == 0) {
            logger.fine("printing input counter = 0");
            System.out.println("\n" +  """
                    Enter your choice:
                     1 - RMI
                     2 - Socket""");
        } else if (inputCounter == 1) {
            logger.fine("printing input counter = 1");

            if (remoteException) {
                remoteException = false;
                System.out.println("\nAn error occurred while connecting to the server, please check the logs.");
            }
            else if (malformedIp) {
                malformedIp = false;
                System.out.println("\nThe provided IP is not correctly formatted, please type it again.");
                System.out.println("Enter the IP address of the server, leave empty for localhost: ");
            }
            else
                System.out.println("\nEnter the IP address of the server, leave empty for localhost: ");
        } else if (inputCounter == 2) {
            System.out.println("Waiting for serverResponse");
        }
    }





    //Method is empty because this component does not observe anything.
    @Override
    public void cleanObserved() {}

    @Override
    public void refreshObserved() {

    }
}
