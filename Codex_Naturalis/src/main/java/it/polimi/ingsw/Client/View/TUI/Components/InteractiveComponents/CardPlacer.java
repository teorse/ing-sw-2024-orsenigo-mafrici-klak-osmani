package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.*;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPlayCard;

//todo review the code
public class CardPlacer extends InteractiveComponent {
    //ATTRIBUTES
    private int inputCounter;
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
    public CardPlacer(){
        super();
        this.model = ClientModel.getInstance();
        inputCounter = 0;
        cardMaps = CardMaps.getInstance();
        cardsHeld = CardsHeld.getInstance();

        refreshObserved();
    }





    //METHODS
    /**
     * Handles user input for selecting and placing cards on the game board.
     * <p>
     * This method processes user input in a multistep manner to allow the selection
     * and placement of cards held by the player. It guides the user through the following
     * steps:
     * 1. Selecting a card from the player's hand.
     * 2. Choosing the row coordinate for card placement.
     * 3. Choosing the column coordinate for card placement.
     * 4. Confirming the card placement and whether the card should be placed face up or down.
     *
     * @param input the user input to be processed.
     * @return a boolean indicating whether the input has been successfully processed and the action completed.
     * <p>
     * The method performs the following steps:
     * 1. Validates the card selection input and increments the input counter.
     * 2. Validates the row coordinate input and increments the input counter.
     * 3. Validates the column coordinate input and increments the input counter, checking for valid placement.
     * 4. Confirms card playability and placement orientation, sending the action if valid.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {

        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        int maxBoardSide = (cardMaps.maxCoordinate() * 2) + 3;

        if (inputCounter == 0) {
            if (InputValidator.checkInputBound(input,1, cardsHeld.getAmountHeld())) {
                cardIndex = Integer.parseInt(input) - 1;
                inputCounter++;
            }
            else
                invalidCardIndex = true;
            return InteractiveComponentReturns.INCOMPLETE;

        } else if (inputCounter == 1) {
            if (input.length() == 1 && InputValidator.isCharWithinBounds(input.toUpperCase().charAt(0),'A', 'A' + maxBoardSide - 1)) {
                chosenRow = input.charAt(0);
                inputCounter++;
            }
            else
                invalidRow = true;
            return InteractiveComponentReturns.INCOMPLETE;

        } else if (inputCounter == 2) {
            if (input.length() == 1 && InputValidator.isCharWithinBounds(input.toUpperCase().charAt(0),'A', 'A' + maxBoardSide - 1)) {
                chosenCol = input.charAt(0);
                inputCounter++;
                int coordinatesChosen = cardMaps.coordinateIndexByCharIndexes(chosenRow, chosenCol, MyPlayer.getInstance().getUsername());

                if(coordinatesChosen == -1){
                    wrongCoordinate = true;
                    inputCounter = 1;
                }
                else
                    coordinateIndex = coordinatesChosen;
            }
            else
                invalidColumn = true;
            return InteractiveComponentReturns.INCOMPLETE;

        } else if (inputCounter == 3) {
            boolean cardPlayability = CardsHeld.getInstance().getCardPlayability(cardIndex);

            if (InputValidator.validBinaryChoice(input)) {
                faceUp = (Integer.parseInt(input) == 1);
                if (cardPlayability || !faceUp) {
                    sendPacket();
                    return InteractiveComponentReturns.COMPLETE;
                }
                else {
                    invalidCardSide = true;
                }
            } else
                invalidBinaryChoice = true;
            return InteractiveComponentReturns.INCOMPLETE;

        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "Place";
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, cardsHeld);
        RefreshManager.getInstance().removeObserved(this, cardMaps);
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, cardsHeld);
        RefreshManager.getInstance().addObserved(this, cardMaps);
    }

    @Override
    public void print(){
        if(inputCounter == 0){
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

        if(invalidCardSide){
            invalidCardSide = false;
            System.out.println("\nThis card can't be played face up. Select the other side or change card!");
        }
        else if (invalidBinaryChoice) {
            invalidBinaryChoice = false;
            System.out.println("The number provided is not a valid input.\nPlease type a number between 1 and 2.");
        }
        else if (wrongCoordinate) {
            wrongCoordinate = false;
            System.out.println("\nThe coordinates you entered are not in the available placements! Try again.");
        }
        else if (invalidColumn) {
            invalidColumn = false;
            System.out.println("The colum provided is not a valid column.\nPlease type a valid column");
        }
        else if (invalidRow) {
            invalidRow = false;
            System.out.println("The row provided is not a valid row.\nPlease type a valid row");
        }
        else if (invalidCardIndex){
            invalidCardIndex = false;
            System.out.println("The card index provided is not a valid index.\nPlease type a valid index");
        }
    }

    private void sendPacket(){
        model.getClientConnector().sendPacket(new CSPPlayCard(cardIndex, coordinateIndex, faceUp));
    }
}
