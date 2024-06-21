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
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

import java.util.List;
import java.util.logging.Logger;

public class LobbyJoiner extends InteractiveComponent {

    private final LobbyPreviews lobbyPreviews;
    private ErrorsDictionary joinLobbyError;
    private final ClientModel model;
    private final Logger logger;

    public LobbyJoiner() {
        super(0);
        logger = Logger.getLogger(LobbyJoiner.class.getName());
        logger.info("Initializing LobbyJoiner Component");
        
        this.lobbyPreviews = LobbyPreviews.getInstance();
        this.model = ClientModel.getInstance();
        joinLobbyError = null;

        refreshObserved();
    }

    @Override
    public InteractiveComponentReturns handleInput(String input) {
        logger.info("Handling the input in Lobby Joiner Component");
        logger.fine("Current input is: "+input);
        if (getInputCounter() == 0) {
            joinLobbyError = null;

            InteractiveComponentReturns superReturn = super.handleInput(input);
            if(superReturn == InteractiveComponentReturns.QUIT)
                return superReturn;
            else if (superReturn == InteractiveComponentReturns.COMPLETE) {
                return InteractiveComponentReturns.INCOMPLETE;
            }

            logger.fine("Checking if lobby preview are not null and not empty");
            if(lobbyPreviews.getLobbyPreviews() != null && !lobbyPreviews.getLobbyPreviews().isEmpty()) {

                logger.fine("Lobby preview are not null or not empty, sending join lobby request to server");

                model.getClientConnector().sendPacket(new CSPJoinLobby(input));
                incrementInputCounter();
                return InteractiveComponentReturns.COMPLETE;
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
        if(joinLobbyError == null) {
            joinLobbyError = model.getJoinLobbyError();
            resetInputCounter();
        }


        if (getInputCounter() == 0) {
            System.out.println("\nLOBBY PREVIEWS");

            if(lobbyPreviews.getLobbyPreviews() != null && !lobbyPreviews.getLobbyPreviews().isEmpty()) {
                for (LobbyPreviewRecord lobbyPreviewRecord : lobbyPreviews.getLobbyPreviews())
                    new LobbyPreviewView(lobbyPreviewRecord).print();

                if(joinLobbyError != null){
                    System.out.println("\nThe following error occurred while joining the lobby:");
                    switch (joinLobbyError) {
                        case GENERIC_ERROR -> System.out.println("Generic error.");
                        case LOBBY_IS_CLOSED -> System.out.println("The lobby was closed.");
                        case LOBBY_NAME_NOT_FOUND -> System.out.println("The provided name does not exist in the server.");
                    }
                }

                System.out.println("\n" + "Enter the name of the lobby you want to join: ");
            }
            else
                System.out.println("""
    
                        There are no lobbies to join at the moment.
                        Please go back and create a lobby or wait \
                        for someone else to create one.""");
        } else if (getInputCounter() > 0) {
            System.out.println("\n waiting for Server Response.");
        }
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
