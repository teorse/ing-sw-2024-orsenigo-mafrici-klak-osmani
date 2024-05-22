package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.LobbyPreviewRecord;
import Client.View.TextUI;
import Client.View.UserInterface;
import Network.ClientServer.Packets.CSPJoinLobby;
import Network.ClientServer.Packets.CSPStartLobby;
import Network.ClientServer.Packets.CSPStopViewingLobbyPreviews;
import Network.ClientServer.Packets.CSPViewLobbyPreviews;

/**
 * The LobbySelectionState represents the state where the client is presented with options to create or join a lobby.
 *
 * <p>In this state, the user can choose to either create a new lobby or join an existing one. If the user chooses to
 * create a lobby, they will be prompted to enter the name of the lobby and the desired number of users for the game.
 * If the user chooses to join a lobby, they will be shown a list of available lobbies along with their details, and
 * prompted to enter the name of the lobby they want to join.
 *
 * <p>Once the user completes the necessary input, the nextState() method is invoked to transition to the next state
 * based on the success of the operation.
 */
public class LobbySelectionState extends ClientState {
    int targetNumberUsers;
    String lobbyName;
    int choice;

    /**
     * Constructs a new LobbySelectionState with the specified client model.
     *
     * @param model the client model
     */
    public LobbySelectionState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

    /**
     * Prints the appropriate message to the user based on the current step of the input sequence.
     *
     * <p>This method displays different messages depending on the value of {@code inputCounter}:
     * <ul>
     *   <li>Step 0: Prompts the user to enter a choice to either create a lobby or join a lobby.</li>
     *   <li>Step 1:
     *     <ul>
     *       <li>If the choice is 1 (create lobby), prompts the user to enter a lobby name.</li>
     *       <li>If the choice is 2 (join lobby), prints the available lobbies with their details and prompts the user to enter the name of the lobby they want to join.</li>
     *     </ul>
     *   </li>
     *   <li>Step 2 (only if the choice is 1): Prompts the user to enter the desired number of users for the game.</li>
     * </ul>
     */
    @Override
    public void print() {
        if (inputCounter == 0) {
            System.out.println("""
                    Enter your choice:
                     1 - Create a lobby
                     2 - Join a lobby""");
        } else if (inputCounter == 1) {
            if (choice == 1) {
                System.out.println("\nEnter lobby name: ");
            } else if (choice == 2) {
                System.out.println("LOBBY PREVIEWS");
                //For loop for printing lobbies
                for (LobbyPreviewRecord lobbyPreviewRecord : model.getLobbyPreviewRecords()) {
                    System.out.print("\nLobby Name: " + lobbyPreviewRecord.lobbyName() + ",  ");
                    System.out.print("Players: " + lobbyPreviewRecord.currentUsers() + "/" + lobbyPreviewRecord.maxUsers() + ",   ");
                    if(lobbyPreviewRecord.gameStarted()) {
                        System.out.println("Game started");
                    } else {
                        System.out.println("Game not started");
                    }
                }
                System.out.println("\n" + "Enter the name of the lobby you want to join: ");
            }
        } else if (inputCounter == 2)
            if (choice == 1)
                System.out.println("Enter the number of wanted users in the game: ");
    }

    /**
     * Handles the input provided by the user.
     *
     * <p>This method processes the input based on the current step of the input sequence:
     * <ul>
     *   <li>Step 0: Expects a binary choice (1 or 2) to determine the type of operation (create lobby or join lobby). If valid, sends a packet to view lobby previews if the choice is 2, increments the input counter, and calls print().</li>
     *   <li>Step 1:
     *     <ul>
     *       <li>If the choice is 1 (create lobby), expects a valid lobby name. If valid, stores it, increments the input counter, and calls print().</li>
     *       <li>If the choice is 2 (join lobby), expects a valid lobby name. If valid, sends a packet to join the lobby; otherwise, calls print().</li>
     *     </ul>
     *   </li>
     *   <li>Step 2 (only if the choice is 1): Expects a valid number of users (between 2 and 4). If valid, stores the number of users and sends a packet to start the lobby; otherwise, calls print().</li>
     * </ul>
     *
     * @param input the input provided by the user
     */
    @Override
    public void handleInput(String input) {
        if (inputCounter == 0) {
            if (TextUI.getBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                if (choice == 2)
                    model.getClientConnector().sendPacket(new CSPViewLobbyPreviews());
                inputCounter++;
            }
            print();
        } else if (inputCounter == 1) {
                if (choice == 1) {
                    if (!TextUI.isNameInvalid(input)) {
                        lobbyName = input;
                        inputCounter++;
                    }
                    print();
                } else if (choice == 2) {
                    if (!TextUI.isNameInvalid(input))
                        model.getClientConnector().sendPacket(new CSPJoinLobby(input));
                    else
                        print();
                }
        } else if (inputCounter == 2 && choice == 1) {
                if (TextUI.checkInputBound(input,2,4)) {
                    targetNumberUsers = Integer.parseInt(input);
                    model.getClientConnector().sendPacket(new CSPStartLobby(lobbyName, targetNumberUsers));
                } else
                    print();
        }
    }

    /**
     * Moves the client to the next state based on the success of the previous operation.
     *
     * <p>If the previous operation was successful, the client transitions to the LobbyJoined state. If the client
     * previously chose to view lobby previews, it sends a packet to stop viewing lobby previews before transitioning
     * to the LobbyJoined state. If the operation failed, an error message is displayed, and the client is prompted
     * to try again.
     */
    @Override
    public void nextState() {
        if (model.isOperationSuccessful()) {
            if (choice == 2)
                model.getClientConnector().sendPacket(new CSPStopViewingLobbyPreviews());
            model.setClientState(new LobbyJoined(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            inputCounter = 1;
            print();
        }
    }
}
