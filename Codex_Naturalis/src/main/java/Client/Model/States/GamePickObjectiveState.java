package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.ObjectiveRecord;
import Client.View.TextUI;
import Network.ClientServer.Packets.CSPPickObjective;

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
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

    /**
     * Prints the available secret objective cards for selection.
     * <p>
     * This method displays each secret objective card along with its index in the list of objective candidates.
     * It iterates through the objective candidates and prints each objective card using the TextUI's method showObjective().
     */
    @Override
    public void print() {
        int i = 1;
        System.out.println("Choose a secret objective: ");
        for (ObjectiveRecord objectiveRecord : model.getObjectiveCandidates()) {
            System.out.print(i + " - ");
            textUI.showObjective(objectiveRecord);
            System.out.println("\n");
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
        if (TextUI.getBinaryChoice(input)) {
            int choice = Integer.parseInt(input);
            model.getClientConnector().sendPacket(new CSPPickObjective(choice - 1));
        } else
            print();
    }

    @Override
    public void nextState() {
        nextGameState();
    }
}
