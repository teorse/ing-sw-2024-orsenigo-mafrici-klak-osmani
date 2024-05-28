package it.polimi.ingsw.Client.Model.States;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.UserInterface;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Client.View.TextUI;
import it.polimi.ingsw.Server.Model.Game.Player.PlayerStates;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPChangeColor;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPQuitLobby;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStartGame;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.logging.Logger;

/**
 * Represents the state of a client after joining a lobby.
 * This state displays the list of users currently in the lobby along with their details.
 * If the current user is identified as the admin, they are prompted to enter "START" to start the game.
 * Handles user input to initiate the game if the current user is the admin.
 * Moves the client to the next state based on the success of the previous operation and the player's current state.
 * If the previous operation was successful and the player's state is set to "PLACE", the client transitions to the GameStarterChoice state.
 * Otherwise, an error message is displayed, and the client remains in the LobbyJoined state, prompting the user to try again.
 */
public class LobbyJoined extends ClientState{

    private final Logger logger;

    /**
     * Constructs a new LobbyJoined state with the specified client model.
     *
     * @param model the client model
     */
    public LobbyJoined(ClientModel model) {
        super(model);
        logger = Logger.getLogger(LobbyJoined.class.getName());
        logger.info("Initializing LobbyJoined");

        print();

        nextState();
    }

    /**
     * Prints the current state of the lobby to the console.
     *
     * <p>This includes the list of users in the lobby with their details such as username, role, color,
     * and connection status. Additionally, it lists the available colors if there are any, and provides
     * instructions on how to change the color by typing its corresponding number. If the user is the
     * admin, it informs them that they can type "START" to begin the game.</p>
     *
     * <p>This method overrides the print method to display the lobby information in a user-friendly format.</p>
     */
    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();

        System.out.println("\nIf you want to go back at the previous choice, type BACK. If you want to quit the lobby, type QUIT.");
        System.out.print("If you want to see the Chat State, type CHAT.");
        if (model.isNewMessage())
            System.out.println(" (NEW MESSAGE)");
        else
            System.out.println();

        System.out.println("\nList of users in the lobby: ");

        for (LobbyUserRecord user : model.getLobbyUserRecords()) {
            System.out.println("\nUsername: " + user.username());
            System.out.println("Role: " + user.role().name());
            System.out.println("Color: " + user.color().name());
            System.out.println("Connection Status: " + user.connectionStatus());
        }

        if (!model.getLobbyRecord().availableUserColors().isEmpty()) {
            System.out.println("\nAvailable colors: ");
            int i = 1;
            for (LobbyUserColors color : model.getLobbyRecord().availableUserColors()) {
                System.out.println(i++ + " - " + color.name());
            }
            System.out.println("\nIf you want to change the color type the one you want by typing its number.");
        }

        if(model.getLobbyUserRecords().size() == model.getLobbyRecord().targetNumberUsers())
            System.out.println("\nTarget number of users reached. Game will start in 20 seconds.");

        if (TextUI.areYouAdmin(model)) {
            if(model.getLobbyUserRecords().size() >= 2)
                System.out.println("\nEnough players in the lobby, type START to start the game!");
            else
                System.out.println("\nNot enough players to start the game, please wait!");
        } else
            System.out.println("\nPlease wait for the Admin to start the game.");
    }

    /**
     * Handles user input for lobby interactions.
     *
     * <p>This method processes the input commands provided by the user and performs the appropriate actions based on the input.
     * If the input is "QUIT", the user is removed from the lobby and the client state is set to lobby selection.
     * If the input is a valid color selection within the available user colors, the user's color is changed accordingly.
     * If the user is the admin and types "START", the game is started. If the user is not the admin, an appropriate message is displayed.</p>
     *
     * @param input The input command provided by the user.
     */
    @Override
    public void handleInput(String input) {
        // If input is to quit the lobby
        if (input.equalsIgnoreCase("QUIT")) {
            model.quitLobby();
            // Send packet to quit lobby
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            // Set client state to LobbySelectionState
            model.setClientState(new LobbySelectionState(model));
        } else if (input.equalsIgnoreCase("CHAT")) {
            model.setClientState(new ChatState(model, this));
        }
        // If input is to change color
        else if (UserInterface.isParsableAsInt(input) && TextUI.checkInputBound(input, 1, model.getLobbyRecord().availableUserColors().size())) {
            // Send packet to change color
            model.getClientConnector().sendPacket(new CSPChangeColor(model.getLobbyRecord().availableUserColors().get(Integer.parseInt(input) - 1)));
        }
        // If user is admin
        else if (TextUI.areYouAdmin(model) && !TextUI.checkInputBound(input, 1, model.getLobbyRecord().availableUserColors().size())) {
            if (model.getLobbyUserRecords().size() >= 2) {
                // If input is to start the game
                if (input.equalsIgnoreCase("START")) {
                    // Send packet to start the game
                    model.getClientConnector().sendPacket(new CSPStartGame());
                } else {
                    // Prompt to start the game
                    System.out.println("Wrong command, to start the game type START!");
                }
            } else {
                System.out.println("Please wait until there are enough players to start the game.");
            }
        }
            // If user is not admin
        else if (!TextUI.areYouAdmin(model) && input.equalsIgnoreCase("START")) {
                // Prompt to wait until the game starts
                System.out.println("Only the admin can start the game, please wait.");
            }
        }

    /**
     * Transitions the client to the next state based on the current game status and player state.
     *
     * <p>This method determines the next state of the client based on whether the game has started and the current state of the player.
     * It logs the current status and then performs state transitions accordingly:</p>
     * <ul>
     *   <li>If the game has started and the player has a defined state, the method switches the client to the appropriate game state:
     *     <ul>
     *       <li>PLACE: Transitions to {@code GameStarterChoice} if the setup is not finished, otherwise to {@code GamePlaceState}.</li>
     *       <li>DRAW: Transitions to {@code GameDrawState}.</li>
     *       <li>DRAW_GOLDEN, DRAW_RESOURCE: Transitions to {@code GameInitialDrawState}.</li>
     *       <li>PICK_OBJECTIVE: Transitions to {@code GamePickObjectiveState}.</li>
     *       <li>WAIT: Transitions to {@code GameWaitState}.</li>
     *     </ul>
     *   </li>
     *   <li>If the player state is not defined or conditions are not met for state transition, logs a message and remains in the current state.</li>
     * </ul>
     */
    @Override
    public synchronized void nextState() {
        logger.info("Choosing next state");
        logger.fine("Current gameStarted: " + model.isGameStarted() +
                "\nCurrent ClientPlayerState: " + model.getMyPlayerGameState());

        PlayerStates myPlayerGameState = model.getMyPlayerGameState();
        if (model.isGameStarted()) {
            if (myPlayerGameState != null) {
                switch (myPlayerGameState) {
                    case PLACE -> {
                        if (!model.isSetUpFinished())
                            model.setClientState(new GameStarterChoice(model));
                        else
                            model.setClientState(new GamePlaceState(model));
                    }
                    case DRAW -> model.setClientState(new GameDrawState(model));
                    case PICK_OBJECTIVE -> model.setClientState(new GamePickObjectiveState(model));
                    case WAIT -> model.setClientState(new GameWaitState(model));
                }
            } else {
                logger.fine("Conditions were not met to switch state, staying in LobbyJoined");
                print();
            }
        }
    }
}
