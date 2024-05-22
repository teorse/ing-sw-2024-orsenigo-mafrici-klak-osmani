package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.ErrorDictionary.ErrorDictionaryLogIn;
import Client.Model.ErrorDictionary.ErrorDictionarySignUp;
import Client.View.TextUI;
import Client.View.UserInterface;
import Network.ClientServer.Packets.CSPLogIn;
import Network.ClientServer.Packets.CSPSignUp;

import java.util.ArrayList;
import java.util.List;

//todo add possibility to go back to the choice of log-in/sign-up

/**
 * The LogInSignUpState represents the state of the client where the user is prompted to log in or sign up.
 *
 * <p>In this state, the user is asked to provide their username and password. If the user chooses to log in,
 * their credentials are validated, and a login packet is sent to the server. If the user chooses to sign up,
 * their credentials are validated, and a signup packet is sent to the server. The client transitions to the
 * next state based on the success of the operation.
 *
 * <p>The state handles user input for logging in or signing up, and it manages the transition to the next state
 * depending on the success of the operation.
 */
public class LogInSignUpState extends ClientState {
    List<String> credentials = new ArrayList<>();
    int choice;

    /**
     * Constructs a new LogInSignUpState with the specified client model.
     *
     * @param model the client model
     */
    public LogInSignUpState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        System.out.println("If you want to go back at the previous choice, type BACK \n");
        print();
    }

    /**
     * Prints the appropriate prompt message based on the current input counter.
     *
     * <p>If the input counter is 0, it prompts the user to enter their choice between logging in or signing up.
     * If the input counter is 1, it prompts the user to enter their username.
     * If the input counter is 2, it prompts the user to enter their password.
     */
    @Override
    public void print() {
        if (inputCounter == 0) {
            System.out.println("""
                    Enter your choice:
                     1 - Log in
                     2 - Sign up""");
        } else if (inputCounter == 1) {
            System.out.println("\nEnter username:");
        } else if (inputCounter == 2) {
            System.out.println("\nEnter password:");
        }
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
        if (input.equalsIgnoreCase("BACK")){
            if (inputCounter == 1) {
                inputCounter = 0;
            } else if (inputCounter == 2) {
                inputCounter = 1;
                credentials.clear();
            } else
                print();
        } else if (inputCounter == 0) {
            if (UserInterface.getBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                inputCounter++;
            }
            print();
        } else if (inputCounter == 1) {
                if (UserInterface.isNameValid(input)) {
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

    /**
     * Moves the client to the next state based on the success of the previous operation.
     *
     * <p>If the previous operation was successful, the client transitions to the LobbySelectionState. Otherwise, an error
     * message is displayed, and the client is prompted to try again.
     */
    @Override
    public void nextState() {
        if (model.isLoggedIn()) {
            model.setClientState(new LobbySelectionState(model));
        } else {
            if (choice == 1) {
                switch (model.getErrorDictionaryLogIn()) {
                    case ErrorDictionaryLogIn.WRONG_PASSWORD -> {
                        System.out.println("Wrong password.");
                        credentials.remove(1);
                        inputCounter = 2;
                    }
                    case ErrorDictionaryLogIn.USERNAME_NOT_FOUND -> {
                        System.out.println("Username not found.");
                        credentials.clear();
                        inputCounter = 1;
                    }
                    case ErrorDictionaryLogIn.YOU_ARE_ALREADY_LOGGED_IN -> {
                        System.out.println("You are already logged in.");
                        credentials.clear();
                        inputCounter = 0;
                    }
                    case ErrorDictionaryLogIn.ACCOUNT_ALREADY_LOGGED_IN_BY_SOMEONE_ELSE -> {
                        System.out.println("Account already logged in on another computer.");
                        credentials.clear();
                        inputCounter = 0;
                    }
                }
                print();
            }
            else if (choice == 2) {
                switch (model.getErrorDictionarySignUp()) {
                    case ErrorDictionarySignUp.USERNAME_ALREADY_TAKEN -> System.out.println("Username already taken.");
                    case ErrorDictionarySignUp.GENERIC_ERROR -> System.out.println("Something happened in the server, please try again.");
                }
                credentials.clear();
                inputCounter = 1;
                print();
            }
        }
    }
}