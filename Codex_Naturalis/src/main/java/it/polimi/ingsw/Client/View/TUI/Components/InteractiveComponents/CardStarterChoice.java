package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardsHeld;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.CardStarterView;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPlayCard;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;

public class CardStarterChoice extends InteractiveComponent {
    private final ClientModel model;
    private final CardRecord cardStarter;
    private boolean invalidBinaryChoice;

    public CardStarterChoice() {
        super(0);
        this.model = ClientModel.getInstance();
        //todo check if there is no scenario where .getFirst() will cause null pointer.
        this.cardStarter = CardsHeld.getInstance().getCardsHeld().getFirst();
        invalidBinaryChoice = false;

        refreshObserved();
    }

    /**
     * Handles user input for playing a card on either the front or back side of the game.
     * Sends a packet to the server based on the user's choice of card placement.
     * Also manages validation for binary choices and sets error flags accordingly.
     *
     * @param input The user's input representing their choice of card placement (1 for front, 2 for back).
     * @return {@link InteractiveComponentReturns#COMPLETE} if the operation completes successfully,
     *         {@link InteractiveComponentReturns#INCOMPLETE} otherwise.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        // Process input through superclass method
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if (superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        // Check if the input is a valid binary choice (1 or 2)
        if (InputValidator.validBinaryChoice(input)) {
            // Convert input to boolean (true for front, false for back)
            boolean faceUp = (Integer.parseInt(input) == 1);

            // Send packet to play the card with the chosen orientation
            model.getClientConnector().sendPacket(new CSPPlayCard(0, 0, faceUp));
            return InteractiveComponentReturns.COMPLETE;
        } else {
            // Flag as invalid binary choice if input is not 1 or 2
            invalidBinaryChoice = true;
        }

        return InteractiveComponentReturns.INCOMPLETE;
    }


    @Override
    public String getKeyword() {
        return "placestarter";
    }

    @Override
    public String getDescription() {
        return "";
    }


    /**
     * Prints the view for placing a card on either the front or back side.
     * Displays the card starter view followed by instructions for side selection.
     * Also handles displaying error messages for invalid binary choices.
     */
    @Override
    public void print() {
        // Display the card starter view
        new CardStarterView(cardStarter).print();

        // Print the side selection instructions
        System.out.println("\n" +
                """
                On which side do you want to place the card? Enter your choice:
                 1 - Front
                 2 - Back""");

        // Display error message for invalid binary choices, if applicable
        if (invalidBinaryChoice) {
            invalidBinaryChoice = false;
            System.out.println("The number you provided is not a valid input, please type 1 or 2.");
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().addObserved(this, CardsHeld.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardsHeld.getInstance());
    }
}
