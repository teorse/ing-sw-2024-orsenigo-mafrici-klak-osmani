package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.LobbyPreviews;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyPreviewView;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPJoinLobby;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;

public class LobbyJoiner extends InteractiveComponent {

    private final LobbyPreviews lobbyPreviews;
    private final ClientModel model;

    public LobbyJoiner() {
        this.lobbyPreviews = LobbyPreviews.getInstance();
        this.model = ClientModel.getInstance();
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (TextUI.isNameValid(input)) {
            model.getClientConnector().sendPacket(new CSPJoinLobby(input));
            return InteractiveComponentReturns.COMPLETE;
        } else {
            //TODO system out 1 in Lobby joiner
            System.out.println("Invalid name! Try again!");
            return InteractiveComponentReturns.INCOMPLETE;
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

        System.out.println("\n" + "Enter the name of the lobby you want to join: ");
        }

    @Override
    public void cleanUp() {

    }

}
