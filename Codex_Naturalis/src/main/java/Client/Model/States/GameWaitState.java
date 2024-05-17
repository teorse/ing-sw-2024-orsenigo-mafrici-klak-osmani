package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.PlayerRecord;
import Client.View.TextUI;
import Model.Game.CardPoolTypes;
import Model.Player.PlayerStates;
import Model.Utility.Coordinates;

public class GameWaitState extends ClientState{
    PlayerRecord choosenplayerRecord;
    Coordinates choosenCoordinates;
    int choice;
    public GameWaitState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        print();
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
     *       <li>If choice is 1 (zoom into CardMap), prompts the user to select a player by nickname.</li>
     *       <li>If choice is 2 (zoom into CardHeld), calls the method to zoom into the cards held by the player.</li>
     *       <li>If choice is 3 (zoom into CardPool), prompts the user to select between Resource Pool and Golden Pool.</li>
     *     </ul>
     *   </li>
     *   <li>Step 3 (only if choice is 1): Prompts the user to choose a coordinate to zoom into the card.</li>
     * </ul>
     */
    @Override
    public void print() {
        textUI.showGameBoard();
        if (inputCounter == 0) {
            for (PlayerRecord playerRecord : model.getPlayerRecords()) {
                if (playerRecord.playerState() != PlayerStates.WAIT)
                    System.out.print("It's " + playerRecord.nickname() + "turn. ");
                else
                    System.out.println("Wait! (Enter ZOOM to look at the board)");
            }
        } else if (inputCounter == 1) {
            System.out.println("""
                        Enter what do you want to zoom:
                         1 - CardMap details
                         2 - CardHeld
                         3 - CardPool""");
        }
        else if (inputCounter == 2) {
            if (choice == 1) {
                System.out.println("Select a player nickname to zoom its CardMap by inserting an integer");
                for (PlayerRecord playerRecord : model.getPlayerRecords()) {
                    int i = 1;
                    System.out.println(i++ + " - Player nickname: " + playerRecord.nickname());
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
            System.out.println("Choose a coordinate to zoom the card [ROW] [COLUMN].");
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
                if (TextUI.checkInputBound(input, 1 , model.getPlayerRecords().size())) {
                    choosenplayerRecord = model.getPlayerRecords().get(Integer.parseInt(input));
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
            choosenCoordinates = textUI.getInputCoordinates(input);
            if (choosenCoordinates != null)
                textUI.zoomCardMap(input, myPR);
        } else
            print();
    }

    @Override
    public void nextState() {
        nextGameState();
    }
}
