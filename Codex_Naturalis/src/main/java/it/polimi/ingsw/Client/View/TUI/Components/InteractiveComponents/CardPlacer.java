package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.*;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPlayCard;

//todo review the code
/**
 * This class represents an interactive component responsible for handling the process
 * of placing a card onto the board in the game's client-side user interface (TUI).
 * It manages the selection of a card from the player's hand, choosing a specific row
 * and column on the board, and deciding whether to place the card face up or face down.
 * Error messages are provided for invalid inputs or placements, guiding the user
 * through the card placement process. Upon successful input validation, the appropriate
 * packet is sent to the server for card placement.
 */
public class CardPlacer extends InteractiveComponent {
    //ATTRIBUTES
    private final CardMaps cardMaps;
    private final CardsHeld cardsHeld;
    private final ClientModel model;

    private int cardIndex;
    private char chosenRow;
    private char chosenCol;
    private int coordinateIndex;
    private boolean faceUp;

    private boolean invalidCardIndex;
    private boolean invalidRow;
    private boolean invalidColumn;
    private boolean wrongCoordinate;
    private boolean invalidCardSide;
    private boolean invalidBinaryChoice;





    //CONSTRUCTOR
    /**
     * Constructs a new instance of {@code CardPlacer} interactive component.
     * Initializes the component with a maximum input counter of 3.
     * Retrieves singleton instances of {@code ClientModel}, {@code CardMaps}, and {@code CardsHeld}
     * for managing client-side game data and interactions.
     * Registers itself as an observer of {@code CardsHeld} and {@code CardMaps} to receive updates.
     */
    public CardPlacer(){
        super(3);
        this.model = ClientModel.getInstance();
        cardMaps = CardMaps.getInstance();
        cardsHeld = CardsHeld.getInstance();

        refreshObserved();
    }





    //METHODS
    /**
     * Handles user input during the card placement process, validating each input stage.
     * This method manages the selection of a card from the player's hand, choosing a row
     * and column on the board, and deciding whether to place the card face up or face down.
     * It sends the appropriate packet to the server based on the validated inputs.
     *
     * @param input The user input to handle.
     * @return Returns {@code InteractiveComponentReturns.COMPLETE} if the input handling is
     *         successfully completed and the necessary packet is sent. Returns
     *         {@code InteractiveComponentReturns.INCOMPLETE} if more input is required.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {

        // Process input through superclass method first
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if (superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        // Determine the maximum board side length based on cardMaps
        int maxBoardSide = (cardMaps.maxCoordinate() * 2) + 3;

        // Check the current input stage and validate the input accordingly
        int inputCounter = getInputCounter();
        if (inputCounter == 0) {
            // Stage 1: Select a card from the player's hand
            if (InputValidator.checkInputBound(input, 1, cardsHeld.getAmountHeld())) {
                cardIndex = Integer.parseInt(input) - 1;
                incrementInputCounter();
            } else {
                invalidCardIndex = true;
            }
            return InteractiveComponentReturns.INCOMPLETE;

        } else if (inputCounter == 1) {
            // Stage 2: Choose a row on the board
            if (input.length() == 1 && InputValidator.isCharWithinBounds(input.toUpperCase().charAt(0), 'A', 'A' + maxBoardSide - 1)) {
                chosenRow = input.charAt(0);
                incrementInputCounter();
            } else {
                invalidRow = true;
            }
            return InteractiveComponentReturns.INCOMPLETE;

        } else if (inputCounter == 2) {
            // Stage 3: Choose a column on the board
            if (input.length() == 1 && InputValidator.isCharWithinBounds(input.toUpperCase().charAt(0), 'A', 'A' + maxBoardSide - 1)) {
                chosenCol = input.charAt(0);
                incrementInputCounter();

                // Validate the chosen coordinates against the board
                int coordinatesChosen = cardMaps.coordinateIndexByCharIndexes(chosenRow, chosenCol, MyPlayer.getInstance().getUsername());
                if (coordinatesChosen == -1) {
                    // Invalid coordinate chosen
                    wrongCoordinate = true;
                    decrementInputCounter();
                    decrementInputCounter();
                } else {
                    coordinateIndex = coordinatesChosen;
                }
            } else {
                invalidColumn = true;
            }
            return InteractiveComponentReturns.INCOMPLETE;

        } else if (inputCounter == 3) {
            // Stage 4: Decide whether to place the card face up or face down
            boolean cardPlayability = CardsHeld.getInstance().getCardPlayability(cardIndex);

            if (InputValidator.validBinaryChoice(input)) {
                faceUp = (Integer.parseInt(input) == 1);
                if (cardPlayability || !faceUp) {
                    // Send the appropriate packet based on the chosen options
                    sendPacket();
                    return InteractiveComponentReturns.COMPLETE;
                } else {
                    // Invalid choice to play the card face up
                    invalidCardSide = true;
                }
            } else {
                invalidBinaryChoice = true;
            }
            return InteractiveComponentReturns.INCOMPLETE;
        }

        // Default return statement, should not be reached under normal circumstances
        return InteractiveComponentReturns.INCOMPLETE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "place";
    }

    /**
     * Returns an empty description for the {@code CardPlacer} interactive component.
     *
     * @return An empty string as no specific description is provided.
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * Removes this {@code CardPlacer} instance as an observer from {@code CardsHeld} and {@code CardMaps}
     * in the {@code RefreshManager} instance, stopping updates from these components.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, cardsHeld);
        RefreshManager.getInstance().removeObserved(this, cardMaps);
    }

    /**
     * Adds this {@code CardPlacer} instance as an observer to {@code CardsHeld} and {@code CardMaps}
     * in the {@code RefreshManager} instance, allowing updates from these components to be received.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, cardsHeld);
        RefreshManager.getInstance().addObserved(this, cardMaps);
    }

    /**
     * Prints prompts and error messages based on the current state of the card placement interaction.
     * This method handles different stages of card placement, including selecting a card from hand,
     * choosing a row and column for placement, and deciding the side (front or back) to place the card.
     * It also prints specific error messages for invalid inputs or placements.
     */
    @Override
    public void print() {
        int inputCounter = getInputCounter();

        // Print prompts based on the current input counter
        if (inputCounter == 0) {
            System.out.println("\nChoose a card from your hand to place.");
        } else if (inputCounter == 1) {
            System.out.println("\nChoose a ROW to place the card. (Only white squares with an X are allowed)");
        } else if (inputCounter == 2) {
            System.out.println("\nChoose a COLUMN to place the card. (Only white squares with an X are allowed)");
        } else if (inputCounter == 3) {
            System.out.println("\n" + """
                On which side do you want to place the card? Enter your choice:
                 1 - Front
                 2 - Back""");
        }

        // Print specific error messages based on flags indicating invalid inputs or placements
        if (invalidCardSide) {
            invalidCardSide = false;
            System.out.println("\nThis card can't be played face up. Select the other side or change card!");
        } else if (invalidBinaryChoice) {
            invalidBinaryChoice = false;
            System.out.println("The number provided is not a valid input.\nPlease type a number between 1 and 2.");
        } else if (wrongCoordinate) {
            wrongCoordinate = false;
            System.out.println("\nThe coordinates you entered are not in the available placements! Try again.");
        } else if (invalidColumn) {
            invalidColumn = false;
            System.out.println("The column provided is not a valid column.\nPlease type a valid column");
        } else if (invalidRow) {
            invalidRow = false;
            System.out.println("The row provided is not a valid row.\nPlease type a valid row");
        } else if (invalidCardIndex) {
            invalidCardIndex = false;
            System.out.println("The card index provided is not a valid index.\nPlease type a valid index");
        }

        // Call superclass print method to ensure consistency in printing
        super.print();
    }

    /**
     * Constructs and sends a {@code CSPPlayCard} packet to the server via the client connector.
     * The packet contains information about the card index, coordinates on the board, and whether
     * the card should be placed face up or face down.
     */
    private void sendPacket(){
        model.getClientConnector().sendPacket(new CSPPlayCard(cardIndex, coordinateIndex, faceUp));
    }
}
