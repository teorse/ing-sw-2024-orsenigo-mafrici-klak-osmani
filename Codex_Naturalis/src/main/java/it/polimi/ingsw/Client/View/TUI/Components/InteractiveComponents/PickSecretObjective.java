package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPickObjective;

public class PickSecretObjective extends InteractiveComponent {

    public PickSecretObjective() {
        super(0);
    }

    /**
     * Handles the user's input for choosing a secret objective.
     * Sends a packet to the server with the selected objective index if the input is a valid binary choice.
     *
     * @param input The user's input to select the secret objective (either "1" or "2").
     * @return {@code InteractiveComponentReturns.COMPLETE} if the input is valid and the packet is sent successfully;
     *         {@code InteractiveComponentReturns.INCOMPLETE} otherwise.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        if (InputValidator.validBinaryChoice(input)) {
            // Send a packet to the server with the selected objective index (0 or 1 based on input)
            ClientModel.getInstance().getClientConnector().sendPacket(new CSPPickObjective(Integer.parseInt(input) - 1));
            return InteractiveComponentReturns.COMPLETE;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "picksecretobjective";
    }

    @Override
    public String getDescription() {
        return "";
    }

    /**
     * Prints a prompt for the user to choose a secret objective.
     * This method displays a message instructing the user to select a secret objective.
     */
    @Override
    public void print() {
        System.out.println("\nChoose a secret objective: \n");
    }


    @Override
    public void cleanObserved() {

    }

    @Override
    public void refreshObserved() {

    }
}
