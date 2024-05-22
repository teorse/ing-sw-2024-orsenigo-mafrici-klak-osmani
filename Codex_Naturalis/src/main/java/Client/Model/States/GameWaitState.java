package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.PlayerRecord;
import Client.View.TextUI;
import Model.Game.CardPoolTypes;
import Model.Player.PlayerStates;
import Model.Utility.Coordinates;

import java.util.logging.Logger;

/**
 * Represents the state of the client when waiting for the game to start.
 * Upon entering this state, the command line interface is cleared, and the appropriate message is printed.
 * This state allows users to interact with the game board and zoom into specific elements.
 */
public class GameWaitState extends ClientState{

    String chosenUsername;
    String chosenRow;
    String chosenCol;
    int choice;
    private final Logger logger;

    /**
     * Represents the state of the client when waiting for the game to start.
     * Upon entering this state, the command line interface is cleared, and the appropriate message is printed.
     */
    public GameWaitState(ClientModel model) {
        super(model);
        logger = Logger.getLogger(GameWaitState.class.getName());
        logger.info("Initializing GameWaitState");
        TextUI.clearCMD();
        print();
        logger.fine("GameWaitStateInitialized");
    }

    /**
     * Displays the current game board and provides various options to the user based on the current step of the input sequence.
     *
     * <p>This method shows different messages depending on the value of {@code inputCounter}:
     * <ul>
     *   <li>Step 0: Displays the game board and indicates which player's turn it is or asks the user to wait.</li>
     *   <li>Step 1: Prompts the user to select what they want to zoom into (CardMap details, CardHeld, or CardPool).</li>
     *   <li>Step 2:
     *     <ul>
     *       <li>If choice is 1 (zoom into CardMap), prompts the user to select a player by username.</li>
     *       <li>If choice is 2 (zoom into CardHeld), calls the method to zoom into the cards held by the player.</li>
     *       <li>If choice is 3 (zoom into CardPool), prompts the user to select between Resource Pool and Golden Pool.</li>
     *     </ul>
     *   </li>
     *   <li>Step 3 (only if choice is 1): Prompts the user to choose a coordinate to zoom into the card.</li>
     * </ul>
     */
    @Override
    public void print() {
        if (model.isSetUpFinished()) {
            textUI.showGameBoard();
            if (inputCounter == 0) {
                System.out.println("Wait! (Enter ZOOM to look at the board)");
                for (PlayerRecord playerRecord : model.getPlayers()) {
                    logger.info(playerRecord.username() + ": " + playerRecord.playerState());
                    if (playerRecord.playerState() == PlayerStates.PLACE || playerRecord.playerState() == PlayerStates.DRAW) {
                        System.out.print("It's " + playerRecord.username() + " turn. ");
                    }
                }
            } else if (inputCounter == 1) {
                System.out.println("""
                        Enter what do you want to zoom:
                         1 - CardMap details
                         2 - CardHeld
                         3 - CardPool""");
            } else if (inputCounter == 2) {
                if (choice == 1) {
                    System.out.println("Select a player username to zoom its CardMap by inserting an integer");
                    for (PlayerRecord playerRecord : model.getPlayers()) {
                        int i = 1;
                        System.out.println(i++ + " - Player username: " + playerRecord.username());
                    }
                    System.out.println();
                } else if (choice == 2) {
                    textUI.zoomCardsHeld();
                } else if (choice == 3) {
                    System.out.println("""
                            Enter the pool you want to zoom:
                             1 - Resource Pool
                             2 - Golden Pool""");
                }
            } else if (inputCounter == 3 && choice == 1) {
                System.out.println("Choose a ROW to zoom the card.");
            } else if (inputCounter == 4 && choice == 1) {
                System.out.println("Choose a COLUMN to zoom the card.");
            }
        } else {
            TextUI.displayGameTitle();
            System.out.println("The Set Up is not completed. Please wait!");
        }
    }

    /**
     * Handles user input and navigates through different stages of the zoom feature in the game.
     *
     * <p>This method processes the input based on the current value of {@code inputCounter}:
     * <ul>
     *   <li>Step 0: Checks if the user entered "ZOOM" to proceed to the next step.</li>
     *   <li>Step 1: Validates the input for selecting the zoom option (1 to 3) and increments the counter if valid.</li>
     *   <li>Step 2:
     *     <ul>
     *       <li>If choice is 1: Validates the input for selecting a player by index and increments the counter if valid.</li>
     *       <li>If choice is 3: Validates the binary choice (1 or 2) to zoom into either Resource Pool or Golden Pool and increments the counter if valid.</li>
     *     </ul>
     *   </li>
     *   <li>Step 3 (only if choice is 1): Gets the input coordinates and zooms into the selected card map if the coordinates are valid.</li>
     * </ul>
     *
     * <p>If the input is invalid at any step, it prints the corresponding prompt again.
     *
     * @param input the user input as a String
     */
    @Override
    public void handleInput(String input) {
        int maxBoardSide = (textUI.maxCoordinate() * 2) + 3;

        if (input.equalsIgnoreCase("BACK")){

            if (inputCounter == 1) {
                inputCounter = 0;
            } else if (inputCounter == 2) {
                inputCounter = 1;
            } else if (inputCounter == 3) {
                inputCounter = 2;
            } else if(inputCounter == 4){
                inputCounter = 3;
            }else
                print();
        }

        if (model.isSetUpFinished()) {
            if (inputCounter == 0) {
                if (input.equalsIgnoreCase("ZOOM")) {
                    inputCounter++;
                } else
                    System.out.println("To look at the board elements enter ZOOM");
            } else if (inputCounter == 1) {
                if (TextUI.checkInputBound(input, 1, 3)) {
                    choice = Integer.parseInt(input);
                    inputCounter++;
                }
                print();
            } else if (inputCounter == 2) {
                if (choice == 1) {
                    if (TextUI.checkInputBound(input, 1, model.getPlayers().size())) {
                        chosenUsername = model.getPlayers().get(Integer.parseInt(input)).username();
                        inputCounter++;
                    }
                    print();
                } else if (choice == 3) {
                    if (TextUI.getBinaryChoice(input)) {
                        if (Integer.parseInt(input) == 1)
                            textUI.zoomCardPool(CardPoolTypes.RESOURCE);
                        else
                            textUI.zoomCardPool(CardPoolTypes.GOLDEN);
                        inputCounter++;
                    }
                }
            } else if (inputCounter == 3 && choice == 1) {
                if (input.length() == 1 && TextUI.isCharWithinBounds(input.toUpperCase().charAt(0),'A', 'A' + maxBoardSide)) {
                    chosenRow = input;
                    inputCounter++;
                }
            } else if (inputCounter == 4 && choice == 1) {
                if (input.length() == 1 && TextUI.isCharWithinBounds(input.toUpperCase().charAt(0),'A', 'A' + maxBoardSide)) {
                    chosenCol = input;
                    Coordinates coordinatesChosen = textUI.inputToCoordinates(chosenCol, chosenRow);
                    if (model.getCardMaps().get(chosenUsername).coordinatesPlaced().contains(coordinatesChosen)) {
                        textUI.zoomCardMap(chosenRow, chosenCol, chosenUsername);
                    } else {
                        System.out.println("The coordinates you entered are not in the used placements of the player you selected! Try again.");
                        inputCounter = 1;
                    }
                }
            } else
                print();
        } else
            System.out.println("\nYou can't enter any input in this phase. Wait the game to start!");
    }

    @Override
    public void nextState() {
        logger.info("Choosing next state");

        logger.fine("Operation Successful flag is true");
        logger.fine("Current value of myPR State: " + model.getMyPlayerGameState());

        if(model.isGameOver()){
            model.setClientState(new GameOverState(model));
        }
        else if (model.getMyPlayerGameState() == PlayerStates.WAIT) {
            logger.fine("Staying in Wait state");
            print();
        }
        else if (model.getMyPlayerGameState() == PlayerStates.DRAW_RESOURCE || model.getMyPlayerGameState() == PlayerStates.DRAW_GOLDEN) {
            logger.fine("Entering GameInitialDrawState");
            model.setClientState(new GameInitialDrawState(model));
        }
        else if (model.getMyPlayerGameState() == PlayerStates.PICK_OBJECTIVE) {
            logger.fine("Entering GamePickObjectiveState");
            model.setClientState(new GamePickObjectiveState(model));
        }
        else if (model.getMyPlayerGameState() == PlayerStates.PLACE) {
            logger.fine("Entering GamePlaceState");
            model.setClientState(new GamePlaceState(model));
        }
    }
}
