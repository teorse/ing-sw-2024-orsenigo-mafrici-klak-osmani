package Client.Model.States;

import Client.Model.ClientModel;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Game.CardPoolTypes;
import Model.Player.PlayerStates;
import Network.ClientServer.Packets.CSPDrawCard;

/**
 * Represents the state where players perform their initial draw actions during the game.
 * Upon entering this state, the command-line interface is cleared, the game title is displayed,
 * and the initial draw options are printed.
 * Players can choose to draw cards from either the resource pool or the golden pool.
 * They are prompted to enter a number between 1 and 3 to pick a card from the selected pool.
 */
public class GameInitialDrawState extends ClientState{
    /**
     * Constructs a new GameInitialDrawState with the specified client model.
     * <p>
     * Upon instantiation, this constructor clears the command-line interface, displays the game title,
     * and prints the initial draw state of the game.
     *
     * @param model the client model
     */
    public GameInitialDrawState(ClientModel model) {
        super(model);

        print();
    }

    /**
     * Prints the options for selecting a card from the resource or golden pool.
     * <p>
     * If the input counter is less than 2, it displays the resource pool; otherwise, it displays the golden pool.
     * Additionally, it prompts the user to enter a number between 1 and 3 to pick a card.
     */
    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        if (model.getMyPlayerGameState() == PlayerStates.DRAW_RESOURCE)
            textUI.zoomCardPool(CardPoolTypes.RESOURCE);
        else if (model.getMyPlayerGameState() == PlayerStates.DRAW_GOLDEN)
            textUI.zoomCardPool(CardPoolTypes.GOLDEN);
        System.out.println("\nEnter a number between 1 and 3 to pick a card: ");
    }

    /**
     * Handles user input for drawing a card from the resource or golden pool.
     * <p>
     * This method checks if the input is within the valid range of choices (1 to 3).
     * If the input is valid and the input counter is less than 2, it sends a packet to draw a card from the resource pool.
     * If the input is valid and the input counter is 2 or greater, it sends a packet to draw a card from the golden pool.
     * If the input is invalid, it prompts the user to try again by printing the available options.
     *
     * @param input the user input indicating the choice of card pool and card index
     */
    @Override
    public void handleInput(String input) {
        if (UserInterface.checkInputBound(input,1,3)) {
            int choice = Integer.parseInt(input);
            if (model.getMyPlayerGameState() == PlayerStates.DRAW_RESOURCE)
                model.getClientConnector().sendPacket(new CSPDrawCard(CardPoolTypes.RESOURCE, choice - 2));
            else if (model.getMyPlayerGameState() == PlayerStates.DRAW_GOLDEN)
                model.getClientConnector().sendPacket(new CSPDrawCard(CardPoolTypes.GOLDEN, choice - 2));
        } else
            print();
    }

    /**
     * Moves the client to the next state based on the success of the previous operation and the player's current state.
     * If the previous operation was successful and the player's state is set to "DRAW", increments the input counter and prints.
     * If the player's state is "PICK_OBJECTIVE", transitions to the GamePickObjectiveState.
     * If the operation fails, prints an error message and remains in the current state, prompting the user to try again.
     */
    @Override
    public void nextState() {
        if(model.isGameOver()){
            model.setClientState(new GameOverState(model));
        }
        else if (model.getMyPlayerGameState() == PlayerStates.PICK_OBJECTIVE)
            model.setClientState(new GamePickObjectiveState(model));
        else if (model.getMyPlayerGameState() == PlayerStates.WAIT)
            model.setClientState(new GameWaitState(model));
        else
            print();
    }
}
