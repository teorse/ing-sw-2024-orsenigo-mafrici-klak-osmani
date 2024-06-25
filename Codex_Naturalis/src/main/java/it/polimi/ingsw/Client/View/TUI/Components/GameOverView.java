package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.GameOver;
import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.List;
import java.util.Map;

/**
 * The GameOverView class is responsible for displaying the game over screen and final rankings.
 * It observes changes in the GameOver model to update the view accordingly.
 */
public class GameOverView extends LiveComponent {

    /**
     * Constructs a new GameOverView and refreshes the observed objects.
     */
    public GameOverView() {
        super();
        refreshObserved();
    }

    /**
     * Prints the game over screen, including the final rankings of players.
     * Displays the winners, their points, objectives completed, and rounds completed.
     */
    @Override
    public void print() {
        TextUI.displayGameOver(); // Display the game over title

        List<PlayerRecord> winners = GameOver.getInstance().getWinners(); // Get the list of winners
        Map<String, LobbyUserColors> colorsMap = LobbyUsers.getInstance().getLobbyUserColorsMap(); // Get the map of user colors

        System.out.println("\nTHE GAME IS OVER. Final Rankings: ");
        int i = 1;
        for (PlayerRecord winner : winners) {
            // Print the winner details
            if (winner.winner()) {
                System.out.print(" 1 - ");
                i++;
            } else {
                System.out.print(" " + i++ + " - ");
            }
            System.out.println(colorsMap.get(winner.username()).getDisplayString() + winner.username() + TerminalColor.RESET +
                    "  Points: " + winner.points() + "  ObjectivesCompleted: " + winner.objectivesCompleted() + " RoundsCompleted: " + winner.roundsCompleted());
        }
        System.out.println("\n" + "Press any key to exit the Final Ranking view."); // Prompt to exit
    }

    /**
     * Cleans the observed objects by removing this view from the observers of GameOver.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, GameOver.getInstance());
    }

    /**
     * Refreshes the observed objects by adding this view to the observers of GameOver.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, GameOver.getInstance());
    }
}
