package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.MyPlayer;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

import java.util.List;

public class PointTableView extends Component {
    private final Players players;
    private final LobbyUsers lobbyUsers;

    public PointTableView() {
        this.players = Players.getInstance();
        this.lobbyUsers = LobbyUsers.getInstance();
    }

    @Override
    public void print() {
        out.println("POINTS TABLE");
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
    public void cleanUp() {

    }
}
