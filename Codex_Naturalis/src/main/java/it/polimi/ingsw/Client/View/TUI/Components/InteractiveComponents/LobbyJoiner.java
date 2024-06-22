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

    /**
     * Handles user input for joining a lobby in the lobby joiner component.
     * Sends a join lobby request to the server if lobby previews are available.
     * Logs input handling details and server interactions.
     *
     * @param input The user input to process.
     * @return InteractiveComponentReturns.COMPLETE if the join lobby request is sent successfully,
     *         InteractiveComponentReturns.INCOMPLETE otherwise.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        // Logging input handling process
        logger.info("Handling the input in Lobby Joiner Component");
        logger.fine("Current input is: " + input);

        // Handle input based on current input counter
        if (getInputCounter() == 0) {
            joinLobbyError = null;

            // Process input through superclass method
            InteractiveComponentReturns superReturn = super.handleInput(input);
            if (superReturn == InteractiveComponentReturns.QUIT)
                return superReturn;
            else if (superReturn == InteractiveComponentReturns.COMPLETE) {
                return InteractiveComponentReturns.INCOMPLETE;
            }

            logger.fine("Checking if lobby previews are not null and not empty");
            // Check if lobby previews are available
            if (lobbyPreviews.getLobbyPreviews() != null && !lobbyPreviews.getLobbyPreviews().isEmpty()) {
                logger.fine("Lobby previews are not null or not empty, sending join lobby request to server");

                // Send join lobby request to the server
                model.getClientConnector().sendPacket(new CSPJoinLobby(input));
                incrementInputCounter();
                return InteractiveComponentReturns.COMPLETE;
            }
        }
        // Return incomplete if conditions are not met
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

    /**
     * Prints the lobby previews and handles user interaction for joining a lobby.
     * Displays lobby previews and any errors encountered while joining a lobby.
     * Allows the user to enter the name of the lobby they want to join.
     */
    @Override
    public void print() {
        // Ensure joinLobbyError is initialized from model
        if (joinLobbyError == null) {
            joinLobbyError = model.getJoinLobbyError();
            resetInputCounter();
        }

        // Handle printing based on current input counter
        if (getInputCounter() == 0) {
            System.out.println("\nLOBBY PREVIEWS");

            // Display lobby previews if available
            if (lobbyPreviews.getLobbyPreviews() != null && !lobbyPreviews.getLobbyPreviews().isEmpty()) {
                for (LobbyPreviewRecord lobbyPreviewRecord : lobbyPreviews.getLobbyPreviews()) {
                    new LobbyPreviewView(lobbyPreviewRecord).print();
                }

                // Display join lobby error message if present
                if (joinLobbyError != null) {
                    System.out.println("\nThe following error occurred while joining the lobby:");
                    switch (joinLobbyError) {
                        case GENERIC_ERROR -> System.out.println("Generic error.");
                        case LOBBY_IS_CLOSED -> System.out.println("The lobby was closed.");
                        case LOBBY_NAME_NOT_FOUND -> System.out.println("The provided name does not exist in the server.");
                    }
                }

                System.out.println("\n" + "Enter the name of the lobby you want to join: ");
            } else {
                // No lobbies available message
                System.out.println("""
                    
                    There are no lobbies to join at the moment.
                    Please go back and create a lobby or wait for someone else to create one.""");
            }
        } else if (getInputCounter() > 0) {
            // Indicate waiting for server response
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
