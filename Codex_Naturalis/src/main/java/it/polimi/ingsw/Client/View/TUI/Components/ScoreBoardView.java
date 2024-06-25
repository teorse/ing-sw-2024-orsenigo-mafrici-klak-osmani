package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.Players;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserConnectionStates;

/**
 * The ScoreBoardView class represents a component responsible for displaying
 * the scoreboard in the TUI. It shows each player's
 * username, their points, and indicates their online status with color-coded asterisks.
 */
public class ScoreBoardView extends LiveComponent {

    /**
     * Constructs a new ScoreBoardView instance.
     */
    public ScoreBoardView() {
        super();
        refreshObserved();
    }

    /**
     * Prints the scoreboard to the terminal.
     * Displays each player's username, points, and their online status with asterisks
     * colored green for online and red for offline.
     */
    @Override
    public void print() {
        Players players = Players.getInstance();
        LobbyUsers lobbyUsers = LobbyUsers.getInstance();

        out.println("SCORE-BOARD");
        for (PlayerRecord playerRecord : players.getPlayers()) {
            // Determine the online status of the player and print asterisks accordingly
            if (lobbyUsers.getLobbyUsersConnectionState(playerRecord.username()) == LobbyUserConnectionStates.ONLINE)
                out.print(TerminalColor.GREEN + "*" + TerminalColor.RESET);
            else
                out.print(TerminalColor.RED + "*" + TerminalColor.RESET);

            // Print the player's username, their color, points, and reset terminal color
            out.println(lobbyUsers.getLobbyUserColors(playerRecord.username()).getDisplayString() +
                    playerRecord.username() + TerminalColor.RESET + ": " + playerRecord.points());
        }
        out.println(); // Print a blank line for separation
    }

    /**
     * Removes this component from being observed by Players and LobbyUsers instances.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Players.getInstance());
        RefreshManager.getInstance().removeObserved(this, LobbyUsers.getInstance());
    }

    /**
     * Adds this component to be observed by Players and LobbyUsers instances for updates.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Players.getInstance());
        RefreshManager.getInstance().addObserved(this, LobbyUsers.getInstance());
    }
}
