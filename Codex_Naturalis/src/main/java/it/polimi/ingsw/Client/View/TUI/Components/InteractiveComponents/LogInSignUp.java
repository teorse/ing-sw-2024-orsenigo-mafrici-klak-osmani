package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPLogIn;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPSignUp;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * The `LogInSignUp` class represents an interactive component responsible for handling
 * user interactions related to logging in or signing up in a text-based user interface (TUI).
 * It manages the process of collecting and validating user input for username and password,
 * sending login or signup requests to the server, and handling errors associated with these operations.
 * <p>
 * This component interacts with the `ClientModel` to retrieve login and signup errors,
 * and communicates with the server via `CSPLogIn` and `CSPSignUp` packets.
 * <p>
 * The class maintains internal state to track the current stage of user input,
 * manages error handling for invalid inputs, and resets input states when necessary.
 */
public class LogInSignUp extends InteractiveComponent {

    private int choice;
    private final List<String> credentials;
    private final ClientModel model;

    private boolean invalidBinaryChoice;
    private boolean invalidUsername;
    private boolean invalidPassword;

    ErrorsDictionary logInError = null;
    ErrorsDictionary signUpError = null;



    /**
     * Constructs a `LogInSignUp` instance with initializations for managing user interactions
     * related to log in and signup processes.
     */
    public LogInSignUp() {
        super(2);
        this.model = ClientModel.getInstance();
        credentials = new ArrayList<>();

        refreshObserved();

        invalidBinaryChoice = false;
        invalidUsername = false;
        invalidPassword = false;
    }

    /**
     * Handles user input for the login or sign-up process.
     * This method processes the input through various stages: binary choice (login or sign-up),
     * username input, and password input. It validates the input at each stage and sends the
     * appropriate packet to the server for login or sign-up.
     *
     * @param input the user input string to be processed
     * @return the state of the input handling process as an InteractiveComponentReturns enum
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        invalidBinaryChoice = false;
        invalidUsername = false;
        invalidPassword = false;
        signUpError = null;
        logInError = null;

        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        int inputCounter = getInputCounter();
        if (inputCounter == 0) {
            // Validate the binary choice input
            if (InputValidator.validBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                incrementInputCounter();
            }
            else
                invalidBinaryChoice = true;

            // If the user is at the username input stage
        } else if (inputCounter == 1) {
            // Validate the username input
            if (InputValidator.isNameValid(input)) {

                //You can't simply use the .add(input) method as this only adds to the tail of the list
                //if the user decides to go back and change the previous input, they would have been unable to do so.
                //By specifiying the index instead only the actual credentials are stored
                credentials.addFirst(input);
                incrementInputCounter();
            }
            else
                invalidUsername = true;

            // If the user is at the password input stage
        } else if (inputCounter == 2) {
            // Validate the password input
            if (InputValidator.isPasswordValid(input)) {

                //You can't simply use the .add(input) method as this only adds to the tail of the list
                //if the user decides to go back and change the previous input, they would have been unable to do so.
                //By specifiying the index instead only the actual credentials are stored
                credentials.add(1, input);


                // If the user chose to log in, send a log in packet
                if (choice == 1) {
                    model.getClientConnector().sendPacket(new CSPLogIn(credentials.get(0), credentials.get(1)));
                    // If the user chose to sign up, send a sign-up packet
                } else {
                    model.getClientConnector().sendPacket(new CSPSignUp(credentials.get(0), credentials.get(1)));
                }
                incrementInputCounter();
                return InteractiveComponentReturns.COMPLETE;
            }
            else
                invalidPassword = true;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "loginsignup";
    }

    /**
     * Returns an empty string as there is no specific description associated with the `LogInSignUp` component.
     *
     * @return An empty string.
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * Prints the login or signup prompts and handles user interaction for collecting
     * username and password inputs. Displays error messages for invalid inputs.
     */
    @Override
    public void print() {
        super.print();

        if(logInError == null)
            logInError = model.getLogInError();
        if(signUpError == null)
            signUpError = model.getSignUpError();

        if(logInError != null || signUpError != null){
            resetInputCounter();
            credentials.clear();
        }

        if (logInError != null) {
            System.out.println("\nThe following error occurred while logging in:");
            switch (logInError) {
                case GENERIC_ERROR -> System.out.println("Generic error.");
                case YOU_ARE_ALREADY_LOGGED_IN -> System.out.println("You are already logged in!");
                case WRONG_PASSWORD -> System.out.println("Wrong password.");
                case USERNAME_NOT_FOUND -> System.out.println("Username not found.");
                case ACCOUNT_ALREADY_LOGGED_IN_BY_SOMEONE_ELSE -> System.out.println("Account already logged in!");
            }
        }

        if (signUpError != null) {
            System.out.println("\nThe following error occurred while signing up:");
            switch (signUpError) {
                case GENERIC_ERROR -> System.out.println("Generic error.");
                case USERNAME_ALREADY_TAKEN -> System.out.println("Username already taken.");
            }
        }

        int inputCounter = getInputCounter();
        if (inputCounter == 0) {
            System.out.println("\n" + """ 
                    Enter your choice:
                     1 - Log in
                     2 - Sign up""");
        } else if (inputCounter == 1) {
            System.out.println("\nEnter username:");
        } else if (inputCounter == 2) {
            System.out.println("\nEnter username:");
            System.out.println(credentials.getFirst());
            System.out.println("\nEnter password:");
        }

        if(invalidBinaryChoice){
            System.out.println("\nThe number you provided is not a valid input.Please type 1 or 2\n");
        }
        else if (invalidUsername) {
            System.out.println("\nThe userName you provided is not a valid username, please try again\n");
        }
        else if (invalidPassword) {
            System.out.println("\nThe password you provided is not a valid password, please try again\n");
        }
    }

    /**
     * Cleans up any observed components or resources associated with the `LogInSignUp` interactive component.
     * This method is intended to handle cleanup tasks specific to observing components, although in the
     * context of `LogInSignUp`, there are no observed components to clean up.
     */
    @Override
    public void cleanObserved() {

    }

    /**
     * Refreshes observed components or resources associated with the `LogInSignUp` interactive component.
     * This method is intended to refresh any state or data that might have changed and needs updating,
     * although in the context of `LogInSignUp`, there are no observed components to refresh.
     */
    @Override
    public void refreshObserved() {

    }
}
