package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.LobbyPreviews;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyPreviewView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPJoinLobby;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;

import java.util.List;

public class LobbyJoiner extends InteractiveComponent {

    private final LobbyPreviews lobbyPreviews;
    private final ClientModel model;

    public LobbyJoiner(ViewState view) {
        super(view);
        this.lobbyPreviews = LobbyPreviews.getInstance();
        this.model = ClientModel.getInstance();

        view.addObserved(this.lobbyPreviews);
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if(lobbyPreviews.getLobbyPreviews() != null) {
            if (InputValidator.isNameValid(input)) {
                model.getClientConnector().sendPacket(new CSPJoinLobby(input));
                return InteractiveComponentReturns.COMPLETE;
            } else {
                //TODO system out 1 in Lobby joiner
                System.out.println("Invalid name! Try again!");
                return InteractiveComponentReturns.INCOMPLETE;
            }
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword () {
        return "JoinLobby";
    }

    @Override
    public void print () {
        System.out.println("\nLOBBY PREVIEWS");

        if(lobbyPreviews.getLobbyPreviews() != null) {
            for (LobbyPreviewRecord lobbyPreviewRecord : lobbyPreviews.getLobbyPreviews())
                new LobbyPreviewView(lobbyPreviewRecord).print();

            System.out.println("\n" + "Enter the name of the lobby you want to join: ");
        }
        else
            System.out.println("There are no lobbies to join at the moment.\nPlease go back and create a lobby or wait " +
                    "for someone else to create one.");
    }


    @Override
    public void cleanObserved() {
        view.removeObserved(lobbyPreviews);
    }
}
