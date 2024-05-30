package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Client.View.UserInterface;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPLogIn;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPSignUp;

import java.util.List;

public class LogInSignUp extends InteractiveComponent {

    int choice;
    List<String> credentials;
    ClientModel model;

    public LogInSignUp() {this.model = ClientModel.getInstance();}

    @Override
    public boolean handleInput(String input) {

        if (inputCounter == 0) {
            // Validate the binary choice input
            if (UserInterface.validBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                inputCounter++;
            }
            // If the user is at the username input stage
        } else if (inputCounter == 1) {
            // Validate the username input
            if (UserInterface.isNameValid(input)) {
                credentials.add(input);
                inputCounter++;
            }
            // If the user is at the password input stage
        } else if (inputCounter == 2) {
            // Validate the password input
            if (UserInterface.isPasswordValid(input)) {
                credentials.add(input);
                // If the user chose to log in, send a log in packet
                if (choice == 1) {
                    model.getClientConnector().sendPacket(new CSPLogIn(credentials.get(0), credentials.get(1)));
                    // If the user chose to sign up, send a sign-up packet
                } else {
                    model.getClientConnector().sendPacket(new CSPSignUp(credentials.get(0), credentials.get(1)));
                }
            }
        }
        return false;
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
    }
}
