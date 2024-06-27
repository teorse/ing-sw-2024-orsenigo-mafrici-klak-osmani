package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPickObjective;

/**
 * The `PickSecretObjective` class represents an interactive component in a text-based user interface (TUI)
 * for allowing a user to choose a secret objective in a game. It handles user input to select between
 * two possible secret objectives and sends the chosen objective to the server via a network packet.
 * This class extends `InteractiveComponent`, which provides common functionality for interactive components
 * in the TUI framework.
 */
public class PickSecretObjective extends InteractiveComponent {

    /**
     * Constructs a new instance of `PickSecretObjective`.
     * Initializes the component with a superclass constructor and sets up for handling user input
     * to pick a secret objective.
     */
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

    /**
     *{@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "picksecretobjective";
    }

    /**
     * Retrieves the description associated with this component.
     * Since this component does not have a specific description to display, it returns an empty string.
     *
     * @return An empty string indicating no specific description.
     */
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
        System.out.println("\nChoose a secret objective:");
    }

    /**
     * Performs cleanup actions related to observed data or resources.
     * This method is not implemented for the `PickSecretObjective` component,
     * as it does not require any cleanup of observed data.
     */
    @Override
    public void cleanObserved() {

    }

    /**
     * Refreshes the observed data or resources related to the component.
     * This method is not implemented for the `PickSecretObjective` component,
     * as it does not observe or refresh any data.
     */
    @Override
    public void refreshObserved() {

    }
}
