package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;

import java.util.List;

public class LobbyPreviewView extends Component {
    private final LobbyPreviewRecord lobbyPreviewRecord;

    public LobbyPreviewView(LobbyPreviewRecord lobbyPreviewRecord) {
        this.lobbyPreviewRecord = lobbyPreviewRecord;
    }

    @Override
    public void print() {
        System.out.print("\nLobby Name: " + lobbyPreviewRecord.lobbyName() + ",  ");
        System.out.print("Players: " + lobbyPreviewRecord.currentUsers() + "/" + lobbyPreviewRecord.maxUsers() + ",   ");
        if(lobbyPreviewRecord.gameStarted()) {
            System.out.println("Game started");
        } else {
            System.out.println("Game not started");
        }
    }
}
