package Client.Model.States;

import Client.Model.ClientModel;
import Client.View.TextUI;
import Client.View.UserInterface;
import Network.ClientServer.Packets.CSPLogIn;
import Network.ClientServer.Packets.CSPSignUp;

import java.util.ArrayList;
import java.util.List;

public class LogInSignUpState extends ClientState {
    List<String> credentials = new ArrayList<>();
    int choice;

    public LogInSignUpState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

    @Override
    public void print() {
        if (inputCounter == 0) {
            System.out.println("""
                    Enter your choice:
                     1 - Log in
                     2 - Sign up""");
        } else if (inputCounter == 1)
            System.out.println("\nEnter username:");
        else if (inputCounter == 2)
            System.out.println("Enter password:");
    }

    /**
     * Handles the input provided by the user.
     *
     * <p>This method processes the input based on the current step of the input sequence:
     * <ul>
     *   <li>Step 0: Expects a binary choice (1 or 2) to determine the type of operation (login or signup). If valid, increments the input counter and calls print().</li>
     *   <li>Step 1: Expects a valid username. If valid, adds it to the credentials list, increments the input counter, and calls print().</li>
     *   <li>Step 2: Expects a valid password. If valid, adds it to the credentials list and sends the appropriate packet (login or signup) based on the user's choice. If invalid, calls print().</li>
     * </ul>
     *
     * @param input the input provided by the user
     */
    @Override
    public void handleInput(String input) {
        if (inputCounter == 0) {
            if (UserInterface.getBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                inputCounter++;
            }
            print();

        } else if (inputCounter == 1) {
                if (!UserInterface.isNameInvalid(input)) {
                    credentials.add(input);
                    inputCounter++;
                }
                print();

        } else if (inputCounter == 2) {
            if (UserInterface.isPasswordValid(input)) {
                credentials.add(input);
                if (choice == 1)
                    model.getClientConnector().sendPacket(new CSPLogIn(credentials.get(0), credentials.get(1)));
                else
                    model.getClientConnector().sendPacket(new CSPSignUp(credentials.get(0), credentials.get(1)));
            } else {
                print();
            }
        }
    }

    @Override
    public void nextState() {
        if (model.isOperationSuccesful()) {
            model.setClientState(new LobbySelectionState(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            credentials.clear();
            inputCounter = 1;
            print();
        }
    }
}
