package Client.Model.States;

import Client.Model.ClientModel;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Game.CardPoolTypes;
import Model.Player.PlayerStates;
import Network.ClientServer.Packets.CSPDrawCard;

/**
 * Represents the state where the player chooses to draw a card from the resource or golden pool during the game.
 * Upon instantiation, the command-line interface is cleared, and the initial draw state of the game is printed.
 * Players are prompted to choose from which pool they want to draw a card.
 * Once the pool selection is made, they are prompted to select a card from the chosen pool.
 * The chosen card is then drawn from the selected pool.
 */
public class GameDrawState extends ClientState{

    CardPoolTypes cardPoolTypes;
    int cardChoice;

    /**
     * Constructs a new GameDrawState with the specified client model.
     * <p>
     * Upon instantiation, this constructor clears the command-line interface and prints the draw state of the game.
     *
     * @param model the client model
     */
    public GameDrawState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        print();
    }

    /**
     * Prints the options for selecting a card from the resource or golden pool.
     * <p>
     * This method displays the available card pools for drawing, including the resource pool and the golden pool.
     * It prompts the user to enter a number to choose from which pool they want to draw a card.
     * If the input counter is 0, it prints the options for selecting a pool (Resource or Golden).
     * If the input counter is 1, it prints nothing, indicating that the user has made their pool selection.
     */
    @Override
    public void print() {
        //TODO fix null pool cards
        textUI.zoomCardPool(CardPoolTypes.RESOURCE);
        textUI.zoomCardPool(CardPoolTypes.GOLDEN);
        if (inputCounter == 0)
            System.out.println("""
                    Enter from which pool you want to draw:
                     1 - Resource
                     2 - Golden""");
        else if (inputCounter == 1)
            System.out.println();
    }

    /**
     * Handles user input to draw a card from the specified card pool.
     *
     * <p>This method processes input based on the current step of the input sequence:
     * <ul>
     *   <li>Step 0: Checks if the input corresponds to a binary choice (1 or 2) to select the card pool type (resource or golden).
     *       If valid, advances to the next step and stores the chosen card pool type.</li>
     *   <li>Step 1: Checks if the input corresponds to a valid card choice index within the specified card pool type.
     *       If valid, advances to the next step and stores the chosen card index.</li>
     * </ul>
     * Once both steps are completed, the method sends a packet to the client connector to draw the selected card from the specified card pool.
     *
     * @param input The user input to be processed.
     */
    @Override
    public void handleInput(String input) {
        if (inputCounter == 0) {
            if (UserInterface.getBinaryChoice(input)) {
                if (Integer.parseInt(input) == 1)
                    cardPoolTypes = CardPoolTypes.RESOURCE;
                else
                    cardPoolTypes = CardPoolTypes.GOLDEN;
                inputCounter++;
            }
            print();
        } else if (inputCounter == 1) {
            if (UserInterface.checkInputBound(input,1,3)) {
                cardChoice = Integer.parseInt(input);
                inputCounter++;
            }
            print();
        } else
            model.getClientConnector().sendPacket(new CSPDrawCard(cardPoolTypes,cardChoice));
    }

    /**
     * Moves the client to the next state based on the success of the previous operation and the player's current state.
     * If the previous operation was successful and the player is not the last online player in the lobby,
     * and the player's state is set to "WAIT", transitions to the GameWaitState to indicate waiting for other players' actions.
     * If the player is the last online player, transitions to the GameOverState to handle the end of the game.
     * If the operation fails, prints an error message and remains in the current state, prompting the user to try again.
     */
    @Override
    public void nextState() {
        if (model.isOperationSuccessful()) {
            if (!UserInterface.isLastOnlinePlayer(model)) {
                if (model.getMyPlayerState() == PlayerStates.WAIT)
                    model.setClientState(new GameWaitState(model));
            } else
                model.setClientState(new GameOverState(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}
