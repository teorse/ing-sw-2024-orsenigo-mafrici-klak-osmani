package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;

/**
 * The LobbyUserView class represents a component responsible for displaying
 * detailed information about a lobby user in the terminal.
 * It prints the username, role, and connection status of the lobby user.
 */
public class LobbyUserView extends Component {

    private final LobbyUserRecord lobbyUser;

    /**
     * Constructs a new LobbyUserView instance with the provided LobbyUserRecord.
     * @param lobbyUser The lobby user record containing information to display.
     */
    public LobbyUserView(LobbyUserRecord lobbyUser) {
        this.lobbyUser = lobbyUser;
    }

    /**
     * Prints detailed information about the lobby user in the terminal.
     * Displays the username with color formatting, role, and connection status.
     */
    @Override
    public void print() {
        System.out.println("\nUsername: " + lobbyUser.color().getDisplayString() + lobbyUser.username() + TerminalColor.RESET);
        System.out.println("Role: " + lobbyUser.role().name());
        System.out.println("Connection Status: " + lobbyUser.connectionStatus());
    }
}
