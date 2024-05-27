package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.ObjectiveRecord;
import Client.View.TextUI;
import Model.Player.PlayerStates;
import Network.ClientServer.Packets.CSPPickObjective;
import Network.ClientServer.Packets.CSPQuitLobby;

import java.util.logging.Logger;

/**
 * Represents the state in which the player picks a secret objective card during the game.
 * <p>
 * Upon entering this state, the command-line interface is cleared, the game title is displayed,
 * and the available secret objective cards are printed for selection.
 * <p>
 * Once instantiated, the player can choose a secret objective card by providing input, and the corresponding
 * packet is sent to the server for processing.
 */
public class GamePickObjectiveState extends ClientState{

    private final Logger logger;

    /**
     * Constructs a new GamePickObjectiveState with the specified client model.
     * <p>
     * Upon instantiation, this constructor clears the command-line interface, displays the game title,
     * and prints the available secret objective cards for selection.
     *
     * @param model the client model
     */
    public GamePickObjectiveState(ClientModel model) {
        super(model);

        logger = Logger.getLogger(GamePickObjectiveState.class.getName());
        logger.info("Initializing GamePickObjectiveState");

        print();

        logger.fine("GamePickObjectiveState initialized");
    }

    /**
     * Prints the available secret objective cards for selection.
     * <p>
     * This method displays each secret objective card along with its index in the list of objective candidates.
     * It iterates through the objective candidates and prints each objective card using the TextUI's method showObjective().
     */
    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        System.out.print("\nIf you want to quit the lobby, type QUIT. If you want to see the Chat State, type CHAT.");
        if (model.isNewMessage())
            System.out.println(" (NEW MESSAGE)");
        else
            System.out.println();

        int i = 1;
        System.out.println("\nChoose a secret objective: \n");
        for (ObjectiveRecord objectiveRecord : model.getObjectiveCandidates()) {
            System.out.print(i + " - ");
            textUI.showObjective(objectiveRecord);
            i++;
        }
    }

    /**
     * Handles user input for picking an objective card.
     * <p>
     * This method checks if the input is a binary choice (1 or 2) indicating which objective card to pick.
     * If the input is valid, it sends a packet to pick the objective card corresponding to the choice.
     * If the input is invalid, it prompts the user to try again by printing the available options.
     *
     * @param input the user input indicating the choice of objective card
     */
    @Override
    public void handleInput(String input) {
        if (input.equalsIgnoreCase("CHAT")) {
            model.setClientState(new ChatState(model,this));
        }
        else if(input.equalsIgnoreCase("QUIT")) {
            model.quitLobby();
            model.getClientConnector().sendPacket(new CSPQuitLobby());
            model.setClientState(new LobbySelectionState(model));
        }
        else if (TextUI.getBinaryChoice(input)) {
            int choice = Integer.parseInt(input);
            model.getClientConnector().sendPacket(new CSPPickObjective(choice - 1));
        } else
            print();
    }

    @Override
    public void nextState() {
        logger.info("Choosing next state");

        logger.fine("Operation Successful flag is true");
        logger.fine("Current value of myPR State: "+ model.getMyPlayerGameState());

        if(model.isGameOver())
            model.setClientState(new GameOverState(model));
        else if (model.getMyPlayerGameState() == PlayerStates.WAIT) {
            logger.fine("Entering GameWaitState");
            model.setClientState(new GameWaitState(model));
        } else if (model.getMyPlayerGameState() == PlayerStates.PLACE) {
            logger.fine("Entering GamePlaceState");
            model.setClientState(new GamePlaceState(model));
        }
        else
            print();
    }
}
