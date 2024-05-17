package Client.Model.States;

import Client.Controller.ClientController;
import Client.Model.ClientModel;
import Client.Network.ClientConnectorRMI;
import Client.Network.ClientConnectorSocket;
import Client.View.TextUI;
import Client.View.UserInterface;

import java.net.SocketTimeoutException;
import java.rmi.ConnectException;

/**
 * Represents the state of establishing a connection to the game server.
 * <p>
 * This state manages the process of selecting the communication method (RMI or Socket) and providing the server's IP address.
 * Upon instantiation, it clears the command-line interface and prompts the user for connection details.
 * It handles user input to choose the communication method and validate the server's IP address.
 * Once the connection is successfully established, it transitions to the LogInSignUpState to handle user authentication.
 *
 * @see LogInSignUpState
 */
public class ConnectionState extends ClientState {

    int choice;

    /**
     * Constructs a new ConnectionState with the specified client model.
     * <p>
     * Upon instantiation, this constructor clears the command-line interface and prints the initial state of the connection.
     *
     * @param model the client model
     */
    public ConnectionState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        print();
    }

    /**
     * Prints the options for selecting the communication method and the server IP address.
     * <p>
     * If the input counter is 0, it prompts the user to choose between RMI and Socket communication methods.
     * If the input counter is 1, it prompts the user to enter the IP address of the server, with an option to leave it empty for localhost.
     */
    @Override
    public void print() {
        if (inputCounter == 0) {
            System.out.println("""
                    Enter your choice:
                     1 - RMI
                     2 - Socket""");
        } else if (inputCounter == 1) {
            System.out.println("\nEnter the IP address of the server, leave empty for localhost: ");
        }
    }

    /**
     * Handles the input provided by the user.
     *
     * <p>This method processes the input based on the current input step.
     * In the first step, it expects a binary choice (1 or 2) to determine
     * the type of connection. In the second step, it validates the provided
     * IP address and attempts to establish a connection using either RMI
     * or Socket based on the user's choice. If the IP address is invalid
     * or the server cannot be found, it prompts the user again.
     *
     * @param input the input provided by the user
     */
    @Override
    public void handleInput(String input) {
        if (inputCounter == 0) {
            if (TextUI.getBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                inputCounter++;
            }
            print();

        } else if (inputCounter == 1) {
            if (input.isEmpty())
                input = "127.0.0.1";

            if (!UserInterface.isValidIPAddress(input))
                print();
            else {
                if (choice == 1) {
                    try {
                        model.setClientConnector(new ClientConnectorRMI(input, new ClientController(model)));
                    } catch (ConnectException connectException) {
                        System.out.println("Server not found!\n");
                        print();
                    }
                } else {
                    try {
                        model.setClientConnector(new ClientConnectorSocket(input, new ClientController(model)));
                    } catch (SocketTimeoutException socketTimeoutException) {
                        System.out.println("Server not found!\n");
                        print();
                    }
                }
            }
        }
    }

    /**
     * Moves the client to the next state based on the success of the previous operation.
     * If the previous operation was successful, transitions to the LogInSignUpState to handle user authentication.
     * If the operation fails, prints an error message and remains in the current state, prompting the user to try again.
     */
    @Override
    public void nextState() {
        if(model.isOperationSuccesful()) {
            model.setClientState(new LogInSignUpState(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}
