package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.GameOver;
import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.List;
import java.util.Map;

public class GameOverView extends LiveComponent{
    ViewState view;

    public GameOverView(ViewState view) {
        super(view);
        this.view = view;
        view.addObserved(GameOver.getInstance());
    }

    @Override
    public void print() {
        List<PlayerRecord> winners = GameOver.getInstance().getWinners();
        Map<String, LobbyUserColors> colorsMap = LobbyUsers.getInstance().getLobbyUserColorsMap();

        System.out.println("\nTHE GAME IS OVER. Final Rankings: ");
        int i = 1;
        for (PlayerRecord winner : winners) {
            if (winner.winner()) {
                System.out.print(" 1 - ");
                i++;
            }
            else
                System.out.print(" " + i++ + " - ");
            System.out.println(colorsMap.get(winner.username()).getDisplayString() + winner.username() + TerminalColor.RESET +
                    "  Points: " + winner.points() + "  ObjectivesCompleted: " + winner.objectivesCompleted() + " RoundsCompleted: " + winner.roundsCompleted());
        }
        System.out.println("\n" + "Press any key to exit the Final Ranking view.");
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, GameOver.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, GameOver.getInstance());
    }
}
