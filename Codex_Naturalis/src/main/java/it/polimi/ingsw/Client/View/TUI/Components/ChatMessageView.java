package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.LobbyUsers;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

public class ChatMessageView extends Component{
    private final ChatMessageRecord message;
    private final LobbyUsers lobbyUsers;

    public ChatMessageView(ChatMessageRecord message){
        this.message = message;
        this.lobbyUsers = LobbyUsers.getInstance();
    }


    @Override
    public void print() {
        // Return the formatted message string
        out.println(message.getTimestamp() + " - " + lobbyUsers.getLobbyUserColors(message.getSender()).getDisplayString() +
                message.getSender() + TerminalColor.RESET + ": " + message.getMessage());
    }

    //Chiamata all'interno di un ciclo for
    //System.out.println(displayMessage(model.getPublicChatMessages().get(i))) Messaggio pubblico
    //System.out.println(displayMessage(model.getPrivateChatMessages().get(chosenUser).get(i))) Messaggio privato
}
