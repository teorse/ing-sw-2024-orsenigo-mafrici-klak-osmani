package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;

public class LobbyView extends Component {
    private final LobbyUsers lobbyUsers;

    public LobbyView(ViewState viewState) {
        super(viewState);
        this.lobbyUsers = LobbyUsers.getInstance();
    }

    @Override
    public void print() {
        System.out.println("\nList of users in the lobby: ");

        for (LobbyUserRecord user : lobbyUsers.getLobbyUserRecords()) {
            new LobbyUserView(view, user).print();
        }
    }

    @Override
    public void cleanUp() {

    }
}
