package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;

public class LobbyView extends LiveComponent {
    private final LobbyUsers lobbyUsers;

    public LobbyView(ViewState view) {
        super();
        this.lobbyUsers = LobbyUsers.getInstance();
        view.addObserved(lobbyUsers);
    }

    @Override
    public void print() {
        System.out.println("\nList of users in the lobby: ");

        if(lobbyUsers.getLobbyUserRecords() != null) {
            for (LobbyUserRecord user : lobbyUsers.getLobbyUserRecords()) {
                new LobbyUserView(user).print();
            }
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, lobbyUsers);
    }
}
