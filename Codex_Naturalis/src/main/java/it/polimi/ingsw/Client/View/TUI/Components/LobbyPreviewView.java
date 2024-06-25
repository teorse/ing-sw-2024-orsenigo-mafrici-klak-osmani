package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyPreviewRecord;

/**
 * The LobbyPreviewView class represents a component responsible for displaying
 * a summarized view of a lobby's information in the terminal.
 * It prints the lobby's name, current number of players, maximum capacity,
 * and whether the game has started or not.
 */
public class LobbyPreviewView extends Component {

    private final LobbyPreviewRecord lobbyPreviewRecord;

    /**
     * Constructs a new LobbyPreviewView instance with the provided LobbyPreviewRecord.
     * @param lobbyPreviewRecord The lobby preview record containing information to display.
     */
    public LobbyPreviewView(LobbyPreviewRecord lobbyPreviewRecord) {
        this.lobbyPreviewRecord = lobbyPreviewRecord;
    }

    /**
     * Prints the lobby's summarized information in the terminal.
     * Displays the lobby name, current number of players, maximum capacity,
     * and indicates whether the game has started or not.
     */
    @Override
    public void print() {
        System.out.print("\nLobby Name: " + lobbyPreviewRecord.lobbyName() + ",  ");
        System.out.print("Players: " + lobbyPreviewRecord.currentUsers() + "/" + lobbyPreviewRecord.maxUsers() + ",   ");
        if (lobbyPreviewRecord.gameStarted()) {
            System.out.println("Game started");
        } else {
            System.out.println("Game not started");
        }
    }
}
