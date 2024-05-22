package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.LobbyUserRecord;
import Client.View.TextUI;
import Model.Player.PlayerStates;
import Network.ClientServer.Packets.CSPQuitLobby;
import Network.ClientServer.Packets.CSPStartGame;

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

        TextUI.clearCMD();
        TextUI.displayGameTitle();
        System.out.println("If you want to go back at the previous choice, type BACK. If you want to quit the lobby, type QUIT \n");
        print();

        nextState();
    }

    /**
     * Prints the list of users currently in the lobby along with their details.
     *
     * <p>The method iterates through the list of lobby users and prints their username, role, color, and connection status.
     * Additionally, if the current user is identified as the admin, it prompts them to enter "START" to start the game.
     */
    @Override
    public void print() {
        System.out.println("List of users in the lobby: ");
        for (LobbyUserRecord user : model.getLobbyUserRecords()) {
            System.out.println("Username: " + user.username());
            System.out.println("Role: " + user.role().name());
            System.out.println("Color: " + user.color().name());
            System.out.println("Connection Status: " + user.connectionStatus() + "\n");
        }
        if (TextUI.areYouAdmin(model))
            System.out.println("You are the Admin, type START to start the game!");
    }

    /**
     * Handles the input provided by the user to start the game.
     *
     * <p>If the current user is identified as the admin, the method checks if the input is "START" to initiate the game.
     * If the input is not "START", it prompts the user to enter "START". If the current user is not the admin, it displays
     * a message indicating that they are not authorized to start the game and should wait until the game starts.
     *
     * @param input the input provided by the user
     */
    @Override
    public void handleInput(String input) {
        if (input.equalsIgnoreCase("QUIT")) {
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setClientState(new LobbySelectionState(model));
        } else if (TextUI.areYouAdmin(model)) {
           if(input.equalsIgnoreCase("START"))
               model.getClientConnector().sendPacket(new CSPStartGame());
           else
               System.out.println("To start the game type START!");
        } else
            System.out.println("You are not the admin. Please wait until the game starts!");
    }

    /**
     * Moves the client to the next state based on the success of the previous operation and the player's current state.
     *
     * <p>If the previous operation was successful and the player's state is set to "PLACE", the client transitions to the
     * GameStarterChoice state. Otherwise, an error message is displayed, and the client remains in the current state,
     * prompting the user to try again.
     */
    @Override
    public synchronized void nextState() {
        logger.info("Choosing next state");
        logger.fine("Current gameStarted: " + model.isGameStarted()+
                "\nCurrent ClientPlayerState: " + model.getMyPlayerGameState());


        PlayerStates myPlayerGameState = model.getMyPlayerGameState();
        if(model.isGameStarted() && myPlayerGameState != null){
            switch(myPlayerGameState){
                case PLACE -> model.setClientState(new GameStarterChoice(model));
                case DRAW -> model.setClientState(new GameDrawState(model));
                case DRAW_GOLDEN, DRAW_RESOURCE -> model.setClientState(new GameInitialDrawState(model));
                case PICK_OBJECTIVE -> model.setClientState(new GamePickObjectiveState(model));
                case WAIT -> model.setClientState(new GameWaitState(model));
            }
        }

        else {
            logger.fine("Conditions were not met to switch state, staying in LobbyJoined");
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}
