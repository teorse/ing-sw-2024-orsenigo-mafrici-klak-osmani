package it.polimi.ingsw.Client.Model.States;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Client.View.TextUI;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPQuitLobby;

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
            if (inputCounter == 0) {
                TextUI.clearCMD();
                if (!model.getGameRecord().lastRoundFlag())
                    TextUI.displayGameTitle();
                else
                    TextUI.displayLastRound();

                System.out.print("\nIf you want to quit the lobby, type QUIT. If you want to see the Chat State, type CHAT.");
                if (model.isNewMessage())
                    System.out.println(" (NEW MESSAGE)\n");
                else
                    System.out.println("\n");

                textUI.showGameBoard();
                System.out.println("Wait! (Type ZOOM if you want to look at the board)");

                if (!model.isWaitingForReconnections()) {
                    for (PlayerRecord playerRecord : model.getPlayers()) {
                        logger.info(playerRecord.username() + ": " + playerRecord.playerState());
                        if (playerRecord.playerState() == PlayerStates.PLACE || playerRecord.playerState() == PlayerStates.DRAW) {
                            System.out.println("\nIt's " + playerRecord.username() + " turn.");
                        }
                    }
                }
                else
                    System.out.println("\nYou're the only player online. Waiting for reconnections!");

            } else if (inputCounter == 1) {
                System.out.println("\n" + """
                        Enter what do you want to zoom:
                         1 - CardMap details
                         2 - CardHeld
                         3 - CardPool""");
            } else if (inputCounter == 2) {
                if (choice == 1) {
                    System.out.println("\nSelect a player username to zoom its CardMap by inserting an integer");
                    int i = 1;
                    for (PlayerRecord playerRecord : model.getPlayers()) {
                        System.out.println(i++ + " - Player username: " + playerRecord.username());
                    }
                } else if (choice == 3) {
                    System.out.println("\n" + """
                            Enter the pool you want to zoom:
                             1 - Resource Pool
                             2 - Golden Pool""");
                }
            } else if (inputCounter == 3 && choice == 1) {
                System.out.println("\nChoose a ROW to zoom the card.");
            } else if (inputCounter == 4 && choice == 1) {
                System.out.println("\nChoose a COLUMN to zoom the card.");
            }
        } else {
            TextUI.clearCMD();
            TextUI.displayGameTitle();
            System.out.println("\nIf you want to see the Chat State, type CHAT.");
            System.out.println("\nThe Set Up is not completed. Please wait!");
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
        // Calculate the maximum board side length
        int maxBoardSide = (textUI.maxCoordinate() * 2) + 3;

        // If input is to go back
        if (input.equalsIgnoreCase("BACK")) {
            // Decrement input counter if it's greater than 0
            if (inputCounter > 0)
                inputCounter--;
            // Print current state
            print();
        }
        else if (input.equalsIgnoreCase("CHAT")) {
            model.setClientState(new ChatState(model,this));
        }
        else if(input.equalsIgnoreCase("QUIT")) {
            model.quitLobby();
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setClientState(new LobbySelectionState(model));
        }
        // If game setup is finished
        else if (model.isSetUpFinished()) {
            // If input counter is 0
            if (inputCounter == 0) {
                // If input is to zoom
                if (input.equalsIgnoreCase("ZOOM")) {
                    // Increment input counter
                    inputCounter++;
                    // Print current state
                    print();
                } else {
                    // Prompt user to enter ZOOM
                    System.out.println("\nTo look at the board elements type ZOOM");
                }
            }
            // If input counter is 1
            else if (inputCounter == 1) {
                // If input is within bound
                if (TextUI.checkInputBound(input, 1, 3)) {
                    // Parse choice
                    choice = Integer.parseInt(input);
                }
                // If choice is 2
                if (choice == 2) {
                    // Zoom into cards held
                    textUI.zoomCardsHeld();
                    inputCounter = 0;
                    System.out.println("\nWait! (Type ZOOM if you want to look at the board)");
                } else {
                    // Increment input counter
                    inputCounter++;
                    print();
                }
            }
            // If input counter is 2
            else if (inputCounter == 2) {
                // If choice is 1
                if (choice == 1) {
                    // If input is within bound
                    if (TextUI.checkInputBound(input, 1, model.getPlayers().size())) {
                        // Choose username
                        chosenUsername = model.getPlayers().get(Integer.parseInt(input) - 1).username();
                        // Increment input counter
                        inputCounter++;
                    }
                    // Print current state
                    print();
                } else if (choice == 3) {
                    // If input is a binary choice
                    if (TextUI.getBinaryChoice(input)) {
                        // Zoom into card pool based on choice
                        if (Integer.parseInt(input) == 1)
                            textUI.zoomCardPool(CardPoolTypes.RESOURCE);
                        else
                            textUI.zoomCardPool(CardPoolTypes.GOLDEN);
                        inputCounter = 0;
                        System.out.println("\nWait! (Type ZOOM if you want to look at the board)");
                    }
                }
            }
            // If input counter is 3 and choice is 1
            else if (inputCounter == 3 && choice == 1) {
                // If input length is 1 and character is within bounds
                if (input.length() == 1 && TextUI.isCharWithinBounds(input.toUpperCase().charAt(0), 'A', 'A' + maxBoardSide - 1)) {
                    // Choose row
                    chosenRow = input;
                    // Increment input counter
                    inputCounter++;
                }
                print();
            }
            // If input counter is 4 and choice is 1
            else if (inputCounter == 4 && choice == 1) {
                // If input length is 1 and character is within bounds
                if (input.length() == 1 && TextUI.isCharWithinBounds(input.toUpperCase().charAt(0), 'A', 'A' + maxBoardSide - 1)) {
                    // Choose column
                    chosenCol = input;
                    // Convert chosen row and column to coordinates
                    Coordinates coordinatesChosen = textUI.inputToCoordinates(chosenRow, chosenCol);
                    // If coordinates are placed, zoom into card map
                    if (model.getCardMaps().get(chosenUsername).coordinatesPlaced().contains(coordinatesChosen)) {
                        textUI.zoomCardMap(chosenRow, chosenCol, chosenUsername);
                        inputCounter = 0;
                        System.out.println("\nWait! (Type ZOOM if you want to look at the board)");
                    } else {
                        // Prompt user to choose valid coordinates
                        System.out.println("The coordinates you entered are not in the used placements of the player you selected! Try again.");
                        // Reset input counter
                        inputCounter = 1;
                    }
                }
            } else {
                // Print current state
                print();
            }
        } else {
            // If setup is not finished, notify user
            System.out.println("\nYou can't enter any input in this phase. Wait for the game to start!");
        }
    }


    @Override
    public void nextState() {
        logger.info("Choosing next state");

        logger.fine("Current value of myPR State: " + model.getMyPlayerGameState());

        if(model.isGameOver()) {
            model.setClientState(new GameOverState(model));
        }
        else if (model.getMyPlayerGameState() == PlayerStates.WAIT) {
            logger.fine("Staying in Wait state");
            print();
        }
        else if (model.getMyPlayerGameState() == PlayerStates.DRAW) {
            logger.fine("Entering GameDrawState");
            model.setClientState(new GameDrawState(model));
        }
        else if (model.getMyPlayerGameState() == PlayerStates.PICK_OBJECTIVE) {
            logger.fine("Entering GamePickObjectiveState");
            model.setClientState(new GamePickObjectiveState(model));
        }
        else if (model.getMyPlayerGameState() == PlayerStates.PLACE) {
            logger.fine("Entering GamePlaceState");
            model.setClientState(new GamePlaceState(model));
        } else
            print();
    }
}







