package Client.Model.States;

import Client.Model.ClientModel;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Player.PlayerStates;
import Model.Utility.Coordinates;

import java.util.logging.Logger;

/**
 * Represents the state where the player places a card on the game board during their turn.
 * Upon entering this state, the command-line interface is cleared, and a message indicating that it's the player's turn is displayed.
 * Additionally, the method prints the available options for placing the card.
 */
public class GamePlaceState extends ClientState{
    int cardIndex;
    int coordinateIndex;
    boolean faceUp;

    private final Logger logger;

    /**
     * Constructs a new GamePlace state with the specified client model
     *
     * @param model the client model
     */
    public GamePlaceState(ClientModel model) {
        super(model);
        logger = Logger.getLogger(GamePlaceState.class.getName());
        logger.info("Initializing GamePlaceState");
        TextUI.clearCMD();
        System.out.println("It's your turn!");
        print();
        logger.fine("GamePlaceState initialized");
    }

    /**
     * Prints the current game board and the cards held by the player.
     * Additionally, it prompts the user to choose a card from their hand to place on the game board.
     * If the input counter is 1, prompts the user to choose a coordinate to place the selected card on the game board.
     * If the input counter is 2, prompts the user to choose the side (front or back) where they want to place the card.
     */
    @Override
    public void print() {
        textUI.showGameBoard();
        textUI.zoomCardsHeld();
        if (inputCounter == 0) {
            System.out.println("Choose a card from your hand to place.");
        } else if (inputCounter == 1) {
            System.out.println("Choose a coordinate to place the card [ROW] [COLUMN]. (On the map are the squares in white with an X)");
        } else if (inputCounter == 2) {
            System.out.println("""
                    On which side do you want to place the card? Enter your choice:
                     1 - Front
                     2 - Back""");
        }
    }

    /**
     * Handles user input based on the current step of the input sequence and the available choices.
     *
     * <p>This method processes input according to the following steps:
     * <ul>
     *   <li>Step 0: Checks if the input corresponds to a valid card index within the player's held cards. If valid, advances to the next step.</li>
     *   <li>Step 1: Obtains coordinates chosen by the user. If valid and available for placement, advances to the next step.</li>
     *   <li>Step 2: Parses the input to determine if the user wants to place the card face up or face down. Advances to the next step upon valid input.</li>
     * </ul>
     * After each step, the method invokes the {@code print} method to display relevant information or prompt the user for further input.
     *
     * @param input The user input to be processed.
     */
    @Override
    public void handleInput(String input) {
        if (inputCounter == 0) {
            if (TextUI.checkInputBound(input,1,3)) {
                cardIndex = Integer.parseInt(input);
                inputCounter++;
            }
        } else if (inputCounter == 1) {
            Coordinates coordinatesChosen = textUI.getInputCoordinates(input);
            if (coordinatesChosen != null && textUI.isAvaiableCoordinates(coordinatesChosen)) {
                coordinateIndex = model.getPlayerCardMapRecord().get(myPR).availablePlacements().indexOf(coordinatesChosen);
                inputCounter++;
            }
        } else if (inputCounter == 2) {
            if (TextUI.getBinaryChoice(input)) {
                faceUp = (Integer.parseInt(input) == 1);
                inputCounter++;
            }
        }
        print();
    }

    /**
     * Moves the client to the next state based on the success of the previous operation and the player's current state.
     * If the previous operation was successful and the player is not the last online player in the game, it checks the player's state.
     * If the player's state is set to "DRAW", transitions to the GameDrawState to allow the player to draw cards.
     * If the player is the last online player, transitions to the GameOverState to handle the end of the game.
     * If the operation fails, prints an error message and remains in the current state, prompting the user to try again.
     */
    @Override
    public void nextState() {
        if (model.isOperationSuccessful()) {
            if (model.getMyPlayerState() == PlayerStates.DRAW) {
                model.setClientState(new GameDrawState(model));
            }
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}

