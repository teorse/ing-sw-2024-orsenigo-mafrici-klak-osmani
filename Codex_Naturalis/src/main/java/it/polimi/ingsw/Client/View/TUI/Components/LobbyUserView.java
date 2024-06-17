package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;

import java.util.List;

public class LobbyUserView extends Component {
    private final LobbyUserRecord lobbyUser;

    public LobbyUserView(LobbyUserRecord lobbyUser) {
        this.lobbyUser = lobbyUser;
    }

    @Override
    public void print() {
        System.out.println("\nUsername: " + lobbyUser.color().getDisplayString() + lobbyUser.username() + TerminalColor.RESET);
        System.out.println("Role: " + lobbyUser.role().name());
        System.out.println("Connection Status: " + lobbyUser.connectionStatus());
    }
}
