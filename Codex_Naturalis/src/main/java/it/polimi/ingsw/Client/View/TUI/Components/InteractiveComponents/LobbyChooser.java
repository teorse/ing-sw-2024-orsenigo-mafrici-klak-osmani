package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.LobbyPreviews;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyPreviewView;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPJoinLobby;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;

public class LobbyChooser extends InteractiveComponent {

    private final LobbyPreviews lobbyPreviews;
    private final ClientModel model;

    public LobbyChooser() {
        this.lobbyPreviews = LobbyPreviews.getInstance();
        this.model = ClientModel.getInstance();
    }

    @Override
    public boolean handleInput(String input) {
        if (TextUI.isNameValid(input)) {
            model.getClientConnector().sendPacket(new CSPJoinLobby(input));
            return true;
        } else {
            System.out.println("Invalid name! Try again!");
            return false;
        }
    }

    @Override
    public String getKeyword () {
        return "";
    }

    @Override
    public void print () {
        System.out.println("\nLOBBY PREVIEWS");
        for (LobbyPreviewRecord lobbyPreviewRecord : lobbyPreviews.getLobbyPreviews())
            new LobbyPreviewView(lobbyPreviewRecord).print();
        }
}
