package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.LobbyPreviews;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.LobbyPreviewView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPJoinLobby;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStopViewingLobbyPreviews;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPViewLobbyPreviews;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;

import java.util.List;

public class LobbyJoiner extends InteractiveComponent {

    private final LobbyPreviews lobbyPreviews;
    private final ClientModel model;

    public LobbyJoiner() {
        super(0);
        this.lobbyPreviews = LobbyPreviews.getInstance();
        this.model = ClientModel.getInstance();

        refreshObserved();
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {

        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

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
        return "joinlobby";
    }

    @Override
    public String getDescription() {
        return "";
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
            System.out.println("""

                    There are no lobbies to join at the moment.
                    Please go back and create a lobby or wait \
                    for someone else to create one.""");
    }


    @Override
    public void cleanObserved() {
        ClientModel.getInstance().getClientConnector().sendPacket(new CSPStopViewingLobbyPreviews());
        RefreshManager.getInstance().removeObserved(this, lobbyPreviews);
    }

    @Override
    public void refreshObserved() {
        ClientModel.getInstance().getClientConnector().sendPacket(new CSPViewLobbyPreviews());
        RefreshManager.getInstance().addObserved(this, lobbyPreviews);
    }
}
