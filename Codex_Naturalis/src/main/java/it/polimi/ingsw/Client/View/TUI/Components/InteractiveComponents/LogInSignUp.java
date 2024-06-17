package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPLogIn;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPSignUp;

import java.util.ArrayList;
import java.util.List;

public class LogInSignUp extends InteractiveComponent {

    private int choice;
    private final List<String> credentials;
    private final ClientModel model;

    private boolean invalidBinaryChoice;
    private boolean invalidUsername;
    private boolean invalidPassword;




    public LogInSignUp(ViewState view) {
        super(view);
        this.model = ClientModel.getInstance();
        credentials = new ArrayList<>();

        invalidBinaryChoice = false;
        invalidUsername = false;
        invalidPassword = false;
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

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
                credentials.add(input);
                inputCounter++;
            }
            else
                invalidUsername = true;

            // If the user is at the password input stage
        } else if (inputCounter == 2) {
            // Validate the password input
            if (InputValidator.isPasswordValid(input)) {
                credentials.add(input);
                // If the user chose to log in, send a log in packet
                if (choice == 1) {
                    model.getClientConnector().sendPacket(new CSPLogIn(credentials.get(0), credentials.get(1)));
                    // If the user chose to sign up, send a sign-up packet
                } else {
                    model.getClientConnector().sendPacket(new CSPSignUp(credentials.get(0), credentials.get(1)));
                }
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
    public void print() {
        if (inputCounter == 0) {
            System.out.println("\n" + """ 
                    Enter your choice:
                     1 - Log in
                     2 - Sign up""");
        } else if (inputCounter == 1) {
            System.out.println("\nEnter username:");
        } else if (inputCounter == 2) {
            System.out.println("\nEnter password:");
        }

        if(invalidBinaryChoice){
            System.out.println("The number you provided is not a valid input.\nPlease type 1 or 2");
        }
        else if (invalidUsername) {
            System.out.println("The userName you provided is not a valid username, please try again");
        }
        else if (invalidPassword) {
            System.out.println("The password you provided is not a valid password, please try again");
        }
    }

    @Override
    public void cleanObserved() {

    }
}
