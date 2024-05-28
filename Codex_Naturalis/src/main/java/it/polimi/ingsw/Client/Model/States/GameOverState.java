package it.polimi.ingsw.Client.Model.States;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Client.View.TextUI;

/**
 * Represents the state of the client when the game is over.
 * It provides functionality to handle user input for exiting the current state and moving to the next state.
 * In this state, the final rankings of players are displayed along with prompts to exit the view.
 */
public class GameOverState extends ClientState {
    /**
     * Constructs a new GameOverState with the specified client model.
     * <p>
     * Upon instantiation, this constructor clears the command-line interface, displays the game over message,
     * and prints the final rankings of the game.
     *
     * @param model the client model
     */
    public GameOverState(ClientModel model) {
        super(model);

        print();
    }

    /**
     * Prints the final rankings of the game.
     * <p>
     * This method displays the final rankings of players based on their points, objectives completed,
     * and rounds completed. Each player's username, points, objectives completed, and rounds completed
     * are shown. Additionally, it prompts the user to exit the final rankings view by entering "EXIT".
     */
    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayGameOver();

        System.out.println("\nTHE GAME IS OVER. Final Rankings: ");
        int i = 1;
        for (PlayerRecord winners : model.getWinners()) {
            if (winners.winner()) {
                System.out.print(" 1 - ");
                i++;
            }
            else
                System.out.print(" " + i++ + " - ");
            System.out.println(winners.username() + "  Points: " + winners.points() + "  ObjectivesCompleted: "
                    + winners.objectivesCompleted() + " RoundsCompleted: " + winners.roundsCompleted());
        }
        System.out.println("\n" + "To exit the Final Rankings view type EXIT.");
    }

    /**
     * Handles the user input for exiting the current state.
     * <p>
     * If the input is "EXIT", transitions to the next state.
     * Otherwise, prints a message prompting the user to enter "EXIT" to exit.
     *
     * @param input the user input
     */
    @Override
    public void handleInput(String input) {
        if(input.equalsIgnoreCase("EXIT")) {
            model.gameOver();
            nextState();
        }
        else
            System.out.println("To exit type EXIT!");
    }

    /**
     * Moves the client to the next state based on the current game conditions.
     * <p>
     * If the current player is the last online player in the lobby, transitions to the LobbySelectionState,
     * allowing the player to select options in the lobby.
     * <p>
     * If there are still other players online in the lobby, transitions to the LobbyJoined state,
     * indicating that the player remains in the lobby and waits for further actions.
     */
    @Override
    public void nextState() {
        model.setClientState(new LobbyJoined(model));
    }
}
