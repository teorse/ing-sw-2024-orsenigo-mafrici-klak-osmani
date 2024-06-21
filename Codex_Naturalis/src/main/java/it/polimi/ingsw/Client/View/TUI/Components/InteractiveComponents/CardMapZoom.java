package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardMaps;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.PlacedCardView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardVisibilityRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

public class CardMapZoom extends InteractiveComponent{
    //ATTRIBUTES
    private final Players players;
    private final CardMaps cardMaps;

    private String chosenCardMapsOwner;
    private char row;
    private char column;
    private CardVisibilityRecord card;





    //CONSTRUCTOR
    public CardMapZoom() {
        super(2);
        players = Players.getInstance();
        cardMaps = CardMaps.getInstance();

        refreshObserved();
    }





    //METHODS

    /**
     * Handles user input for selecting and viewing details of a card on the game board.
     * <p>
     * This method processes user input in a step-by-step manner to allow the selection of a card
     * from another player's card map and displays the details of the selected card.
     * The steps are as follows:
     * 1. Select the player whose card map to view.
     * 2. Choose the row coordinate for the card on the selected player's card map.
     * 3. Choose the column coordinate for the card on the selected player's card map.
     *
     * @param input the user input to be processed.
     * @return a boolean indicating whether the input has been successfully processed and the action completed.
     * <p>
     * The method performs the following steps:
     * 1. Validates the player selection input and increments the input counter.
     * 2. Validates the row coordinate input and increments the input counter.
     * 3. Validates the column coordinate input, retrieves the card from the specified coordinates,
     *    and displays the card details.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        int inputCounter = getInputCounter();

        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        if (inputCounter == 0) {
            if (InputValidator.checkInputBound(input, 1, players.getPlayersSize())) {
                //Username of the chosen cardMap's owner
                chosenCardMapsOwner = players.getUsernameByIndex(Integer.parseInt(input) - 1);
                // Increment input counter
                incrementInputCounter();

                // Print current state
                print();
            }
            else{
                //todo print error message index out of bounds
            }

            return InteractiveComponentReturns.INCOMPLETE;
        }
        else if(inputCounter == 1){
            if (input.length() == 1 && InputValidator.isCharWithinBounds(input.toUpperCase().charAt(0), 'A', 'A' + cardMaps.maxBoardSide() - 1)) {
                // Choose row
                row = input.charAt(0);
                // Increment input counter
                incrementInputCounter();

                // Print current state
                print();
            }
            return InteractiveComponentReturns.INCOMPLETE;
        }
        else if(inputCounter == 2){
            if (input.length() == 1 && InputValidator.isCharWithinBounds(input.toUpperCase().charAt(0), 'A', 'A' + cardMaps.maxBoardSide() - 1)) {
                // Choose row
                column = input.charAt(0);

                Coordinates coordinate = cardMaps.charsToCoordinates(row, column);
                card = cardMaps.getCardByCoordinate(coordinate, chosenCardMapsOwner);

                //print the zoomed card
                new PlacedCardView(card).print();

                // Reset the input counter
                resetInputCounter();
                return InteractiveComponentReturns.COMPLETE;
            }
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "cardmapzoom";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, players);
        RefreshManager.getInstance().removeObserved(this, cardMaps);
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, players);
        RefreshManager.getInstance().addObserved(this, cardMaps);
    }

    @Override
    public void print() {
        int inputCounter = getInputCounter();

        if(inputCounter == 0){
            System.out.println("\nSelect a player username to zoom its CardMap by inserting an integer");
            int i = 1;
            for (PlayerRecord playerRecord : Players.getInstance().getPlayers()) {
                System.out.println(i++ + " - Player username: " + playerRecord.username());
            }
        }
        else if(inputCounter == 1){
            System.out.println("\nChoose a ROW to zoom the card.");
        }
        else if(inputCounter == 2){
            System.out.println("\nChoose a COLUMN to zoom the card.");
        }
    }
}
