package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.GameOver;
import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;

import java.util.List;

public class GameOverView extends Component{
    private List<PlayerRecord> winners;
    private LobbyUsers lobbyUsers;

    public GameOverView() {
        this.winners = GameOver.getInstance().getWinners();
        this.lobbyUsers = LobbyUsers.getInstance();
    }

    @Override
    public void print() {
        System.out.println("\nTHE GAME IS OVER. Final Rankings: ");
        int i = 1;
        for (PlayerRecord winner : winners) {
            if (winner.winner()) {
                System.out.print(" 1 - ");
                i++;
            }
            else
                System.out.print(" " + i++ + " - ");
            System.out.println(lobbyUsers.getLobbyUserColors(winner.username()).getDisplayString() + winner.username() + TerminalColor.RESET +
                    "  Points: " + winner.points() + "  ObjectivesCompleted: " + winner.objectivesCompleted() + " RoundsCompleted: " + winner.roundsCompleted());
        }
        System.out.println("\n" + "Press any key to exit the Final Ranking view.");
    }

    @Override
    public void cleanUp() {

    }
}
