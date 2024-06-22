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
     * Handles user input for zooming into a specific player's CardMap.
     * Manages different input stages to select a player, row, and column to zoom into.
     * Prints appropriate prompts and messages based on the current input stage.
     * Resets the input counter upon completing the zoom operation.
     *
     * @param input The user input provided during the interaction.
     * @return The state of completion of the interactive component.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        int inputCounter = getInputCounter();

        // Process input through superclass method
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if (superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        // Handle input based on the current input stage
        if (inputCounter == 0) {
            // Stage 1: Selecting a player's username to zoom into their CardMap
            if (InputValidator.checkInputBound(input, 1, players.getPlayersSize())) {
                // Retrieve the username of the chosen cardMap's owner
                chosenCardMapsOwner = players.getUsernameByIndex(Integer.parseInt(input) - 1);
                // Increment input counter
                incrementInputCounter();
                // Print current state
                print();
            } else {
                // todo: Print error message for index out of bounds
            }
            return InteractiveComponentReturns.INCOMPLETE;
        } else if (inputCounter == 1) {
            // Stage 2: Choosing a row to zoom into
            if (input.length() == 1 && InputValidator.isCharWithinBounds(input.toUpperCase().charAt(0), 'A', 'A' + cardMaps.maxBoardSide() - 1)) {
                // Select row
                row = input.charAt(0);
                // Increment input counter
                incrementInputCounter();
                // Print current state
                print();
            }
            return InteractiveComponentReturns.INCOMPLETE;
        } else if (inputCounter == 2) {
            // Stage 3: Choosing a column to zoom into
            if (input.length() == 1 && InputValidator.isCharWithinBounds(input.toUpperCase().charAt(0), 'A', 'A' + cardMaps.maxBoardSide() - 1)) {
                // Select column
                column = input.charAt(0);

                // Convert row and column to coordinates
                Coordinates coordinate = cardMaps.charsToCoordinates(row, column);

                // Retrieve the card at the chosen coordinates and owner's CardMap
                card = cardMaps.getCardByCoordinate(coordinate, chosenCardMapsOwner);

                // Print the zoomed card view
                new PlacedCardView(card).print();

                // Reset the input counter for the next interaction
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

    /**
     * Prints the interactive prompts and messages for zooming into a specific player's
     * CardMap. Depending on the current input stage, it displays different instructions
     * for selecting a player and zooming into specific rows and columns of their CardMap.
     * This method utilizes the superclass print method to ensure consistent output.
     */
    @Override
    public void print() {
        int inputCounter = getInputCounter();

        // Print instructions based on the current input stage
        if (inputCounter == 0) {
            // Stage 1: Prompt to select a player username
            System.out.println("\nSelect a player username to zoom its CardMap by inserting an integer");
            int i = 1;
            for (PlayerRecord playerRecord : Players.getInstance().getPlayers()) {
                System.out.println(i++ + " - Player username: " + playerRecord.username());
            }
        } else if (inputCounter == 1) {
            // Stage 2: Prompt to choose a row to zoom into
            System.out.println("\nChoose a ROW to zoom the card.");
        } else if (inputCounter == 2) {
            // Stage 3: Prompt to choose a column to zoom into
            System.out.println("\nChoose a COLUMN to zoom the card.");
        }

        // Call superclass print method for additional output if any
        super.print();
    }
}
