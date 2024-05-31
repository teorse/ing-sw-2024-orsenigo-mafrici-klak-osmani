package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Network.ClientConnectorRMI;
import it.polimi.ingsw.Client.Network.ClientConnectorSocket;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Utils.Utilities;

import java.net.SocketTimeoutException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

public class ServerConnectionWizard extends InteractiveComponent {

    private Logger logger;
    private int choice;

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (inputCounter == 0) {
            if (TextUI.validBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                inputCounter++;
            }
            return false;

        } else if (inputCounter == 1) {
            if (input.isEmpty())
                input = "127.0.0.1";

            if (choice == 1) {
                connectionTimedOut = false;
                remoteException = false;
                try {
                    //todo update controller and connector to use model singleton pattern
                    ClientModel model = ClientModel.getInstance();
                    System.out.println("Attempting connection to server");
                    ClientModel.getInstance().setClientConnector(new ClientConnectorRMI(input, new ClientController(model), model));
                    return true;
                }
                catch (ConnectException connectException) {
                    System.out.println("Connection timed-out!\n" +
                            "Try with another server ip.");
                    String stackTrace = Utilities.StackTraceToString(connectException);
                    logger.warning("Connection timed out while connecting to RMI server: " + input + "\n" +
                            "Stacktrace:\n" + stackTrace);
                }
                catch (RemoteException e) {
                    String stackTrace = Utilities.StackTraceToString(e);
                    logger.warning("RemoteException was thrown while creating ClientConnectorRMI\nStacktrace:\n" + stackTrace);
                    System.out.println("An error occurred while connecting to the server, please check the logs.");
                }

                inputCounter = 0;
                return false;

            } else {
                try {
                    ClientModel model = ClientModel.getInstance();
                    System.out.println("Attempting connection to server");
                    ClientModel.getInstance().setClientConnector(new ClientConnectorSocket(input, new ClientController(model), model));
                    return true;
                } catch (SocketTimeoutException socketTimeoutException) {
                    System.out.println("Server not found!\n");
                }

                inputCounter = 0;
                return false;
            }
        }
        return false;
    }



    @Override
    public String getKeyword() {
        return "connect";
    }

    @Override
    public void print() {
        if (inputCounter == 0) {
            TextUI.clearCMD();
            System.out.println("""
                    Enter your choice:
                     1 - RMI
                     2 - Socket""");
        } else if (inputCounter == 1) {
            if(connectionTimedOut)
                System.out.println("Could not connect to the Server.\nTry with another server ip.");
            else if (remoteException)
                System.out.println("An error occurred while connecting to the server, please check the logs.");
            else
                System.out.println("\nEnter the IP address of the server, leave empty for localhost: ");
        }
    }
}
