package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

public class ScoreBoardView extends LiveComponent {

    public ScoreBoardView(ViewState view) {
        super(view);
        view.addObserved(Players.getInstance());
        view.addObserved(LobbyUsers.getInstance());
    }

    @Override
    public void print() {
        Players players = Players.getInstance();
        LobbyUsers lobbyUsers = LobbyUsers.getInstance();

        out.println("SCORE-BOARD");
        for (PlayerRecord playerRecord : players.getPlayers()) {
            if (lobbyUsers.getLobbyUsersConnectionState(playerRecord.username()) == LobbyUserConnectionStates.ONLINE)
                out.print(TerminalColor.GREEN + "*" + TerminalColor.RESET);
            else
                out.print(TerminalColor.RED + "*" + TerminalColor.RESET);
            out.println(lobbyUsers.getLobbyUserColors(playerRecord.username()).getDisplayString() + playerRecord.username() + TerminalColor.RESET + ": " + playerRecord.points());
        }
        out.println();
    }

    @Override
    public void cleanObserved() {
        view.removeObserved(Players.getInstance());
        view.removeObserved(LobbyUsers.getInstance());
    }
}