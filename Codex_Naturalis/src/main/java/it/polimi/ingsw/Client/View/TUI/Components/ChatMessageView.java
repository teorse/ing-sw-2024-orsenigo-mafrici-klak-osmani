package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

/**
 * The ChatMessageView class is responsible for displaying an individual chat message.
 * It retrieves user information from the LobbyUsers model to color-code the message based on the sender.
 */
public class ChatMessageView extends Component {
    private final ChatMessageRecord message;
    private final LobbyUsers lobbyUsers;

    /**
     * Constructs a new ChatMessageView with the specified chat message.
     *
     * @param message the chat message to be displayed
     */
    public ChatMessageView(ChatMessageRecord message) {
        this.message = message;
        this.lobbyUsers = LobbyUsers.getInstance();
    }

    /**
     * Prints the chat message.
     * The message is formatted with the timestamp, sender's name (color-coded), and the message content.
     */
    @Override
    public void print() {
        // Print the formatted message string
        out.println(message.getTimestamp() + " - " +
                lobbyUsers.getLobbyUserColors(message.getSender()).getDisplayString() +
                message.getSender() + TerminalColor.RESET + ": " +
                message.getMessage());
    }
}
