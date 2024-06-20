package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPLogIn;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPSignUp;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

import java.util.ArrayList;
import java.util.List;

public class LogInSignUp extends InteractiveComponent {

    private int choice;
    private final List<String> credentials;
    private final ClientModel model;

    private boolean invalidBinaryChoice;
    private boolean invalidUsername;
    private boolean invalidPassword;

    ErrorsDictionary logInError = null;
    ErrorsDictionary signUpError = null;




    public LogInSignUp() {
        super();
        this.model = ClientModel.getInstance();
        credentials = new ArrayList<>();

        refreshObserved();

        invalidBinaryChoice = false;
        invalidUsername = false;
        invalidPassword = false;
    }

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

        if (inputCounter == 0) {
            // Validate the binary choice input
            if (InputValidator.validBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                inputCounter++;
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
                inputCounter++;
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
                inputCounter++;
                return InteractiveComponentReturns.COMPLETE;
            }
            else
                invalidPassword = true;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void print() {
        if(logInError == null)
            logInError = model.getLogInError();
        if(signUpError == null)
            signUpError = model.getSignUpError();

        if(logInError != null || signUpError != null){
            inputCounter = 0;
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

    @Override
    public void cleanObserved() {

    }

    @Override
    public void refreshObserved() {

    }
}
