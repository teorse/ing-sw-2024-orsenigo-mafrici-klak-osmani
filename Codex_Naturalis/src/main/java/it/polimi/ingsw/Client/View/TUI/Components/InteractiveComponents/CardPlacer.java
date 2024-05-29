package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardMaps;
import it.polimi.ingsw.Client.Model.CardsHeld;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPPlayCard;

public class CardPlacer extends InteractiveComponent {
    //ATTRIBUTES
    private int inputCounter;
    private final CardMaps cardMaps;
    private final CardsHeld cardsHeld;
    private final ClientModel model;

    int cardIndex;
    char chosenRow;
    char chosenCol;
    int coordinateIndex;
    boolean faceUp;





    //CONSTUCTOR
    public CardPlacer(ClientModel model){
        this.model = model;
        inputCounter = 0;
        cardMaps = CardMaps.getInstance();
        cardsHeld = CardsHeld.getInstance();
    }





    //METHODS
    @Override
    /**
     * Handles user input for selecting and placing cards on the game board.
     * <p>
     * This method processes user input in a multi-step manner to allow the selection
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
    public boolean handleInput(String input) {
        int maxBoardSide = (cardMaps.maxCoordinate() * 2) + 3;

        if (inputCounter == 0) {
            if (TextUI.checkInputBound(input,1, cardsHeld.getAmountHeld())) {
                cardIndex = Integer.parseInt(input) - 1;
                inputCounter++;
            }
            print();
        } else if (inputCounter == 1) {
            if (input.length() == 1 && TextUI.isCharWithinBounds(input.toUpperCase().charAt(0),'A', 'A' + maxBoardSide - 1)) {
                chosenRow = input.charAt(0);
                inputCounter++;
            }
            print();
        } else if (inputCounter == 2) {
            if (input.length() == 1 && TextUI.isCharWithinBounds(input.toUpperCase().charAt(0),'A', 'A' + maxBoardSide - 1)) {
                chosenCol = input.charAt(0);
                inputCounter++;
                int coordinatesChosen = cardMaps.coordinateIndexByCharIndexes(chosenRow, chosenCol, MyPlayer.getInstance().getUsername());

                if(coordinatesChosen == -1){
                    System.out.println("\nThe coordinates you entered are not in the available placements! Try again.");
                    inputCounter = 1;
                }
                else
                    coordinateIndex = coordinatesChosen;
            }
            print();
        } else if (inputCounter == 3) {
            boolean cardPlayability = CardsHeld.getInstance().getCardPlayability(cardIndex);

            if (TextUI.validBinaryChoice(input)) {
                faceUp = (Integer.parseInt(input) == 1);
                if (cardPlayability || !faceUp)
                    sendPacket();
                else {
                    System.out.println("\nThis card can't be played face up. Select the other side or change card!");
                    print();
                }
            } else
                print();
        }
        return false;
    }

    @Override
    public String getKeyword() {
        return "Place";
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
    }

    private void sendPacket(){
        model.getClientConnector().sendPacket(new CSPPlayCard(cardIndex, coordinateIndex, faceUp));
    }
}
