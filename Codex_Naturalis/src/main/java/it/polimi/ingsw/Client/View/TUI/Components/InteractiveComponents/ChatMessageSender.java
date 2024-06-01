package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;


import it.polimi.ingsw.Client.Model.*;
import it.polimi.ingsw.Client.Network.ClientConnector;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.Components.ChatConversationView;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPSendChatMessage;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageSender extends InteractiveComponent implements Observer {
    //ATTRIBUTES
    private final Chat chat;
    private int choice;
    private String chosenUser;
    private final ClientConnector connection;
    private LobbyUsers lobbyUsers;
    private ViewState viewState;

    private final List<String> recipients;

    private ChatConversationView conversationView;
    private ChatMessagesStack conversationInteract;
    private boolean inConversation;

    //ERROR PRINTING VARIABLES
    private boolean emptyMessage = false;
    private boolean invalidBinaryChoice = false;
    private boolean invalidInputBoundChoice = false;




    //CONSTURCTOR
    public ChatMessageSender(ViewState viewState){
        chat = Chat.getInstance();
        this.viewState = viewState;
        chat.subscribe(this);
        lobbyUsers = LobbyUsers.getInstance();
        connection = ClientModel.getInstance().getClientConnector();
        inConversation = false;

        recipients = new ArrayList<>();

        for(int i = 0; i < lobbyUsers.size(); i++){
            String username = lobbyUsers.getLobbyUserNameByIndex(i);
            if(!username.equals(MyPlayer.getInstance().getUsername()))
                recipients.add(username);
        }
    }





    //METHODS
    /**
     * Handles user input for chat interactions, including selecting chat type (public or private),
     * sending chat messages, and selecting private chat recipients.
     *
     * @param input the user's input as a string.
     * @return true if the input results in a successful chat operation, false otherwise.
     * <p>
     * This method processes the input based on several conditions:
     * <p>
     * 1. If the user is in an active conversation (indicated by inConversation):
     *    - If choice is 1 (public chat), it handles the input as a public chat message and sends it.
     *    - If choice is 2 (private chat), it handles the input as a private chat message and sends it.
     * <p>
     * 2. If inputCounter is 0 (initial chat type selection):
     *    - It checks if the input is a valid binary choice (1 for public chat, 2 for private chat).
     *    - If choice is 1, it sets up the public chat view and interaction.
     *    - If choice is 2, it increments inputCounter to proceed to the next input phase.
     * <p>
     * 3. If inputCounter is 1 and choice is 2 (selecting a user for private chat):
     *    - It checks if the input is within the valid range for selecting a private chat recipient.
     *    - If valid, it sets up the private chat view and interaction with the chosen user.
     * <p>
     * If any input is invalid or results in an error, appropriate error messages are displayed,
     * and the method returns false, indicating that the chat operation was not successful.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if(inConversation) {
            if (choice == 1) {
                // Handle public chat message input
                if (!input.trim().isEmpty())
                    connection.sendPacket(new CSPSendChatMessage(new ChatMessageRecord(input.replaceFirst("^\\s+", ""))));
                else
                    emptyMessage = true;
                return InteractiveComponentReturns.INCOMPLETE;
            }

            if (choice == 2) {
                // Handle private chat message input
                if (!input.trim().isEmpty())
                    connection.sendPacket(new CSPSendChatMessage(new ChatMessageRecord(input.replaceFirst("^\\s+", ""), chosenUser)));
                else
                    emptyMessage = true;
                return InteractiveComponentReturns.INCOMPLETE;
            }
        }

        if (inputCounter == 0) {
            // Handle initial choice input (Public or Private Chat)
            if (TextUI.validBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                if (choice == 1) {
                    chat.unsubscribe(this);
                    conversationView = new ChatConversationView(chat.getPublicChat(), viewState);
                    conversationInteract = chat.getPublicChat();
                    conversationInteract.subscribe(this);
                    inConversation = true;
                    return InteractiveComponentReturns.INCOMPLETE;
                }
                inputCounter++;
            } else {
                // Error handling for invalid initial choice input
                invalidBinaryChoice = true;
            }
            return InteractiveComponentReturns.INCOMPLETE;
        } else if (inputCounter == 1 && choice == 2) {
            // Handle selection of a user for private chat
            if (TextUI.checkInputBound(input, 1, lobbyUsers.size() - 1)) {
                chosenUser = recipients.get(Integer.parseInt(input) - 1);

                chat.unsubscribe(this);
                conversationView = new ChatConversationView(chat.getPrivateChat(chosenUser), viewState);
                conversationInteract = chat.getPrivateChat(chosenUser);
                conversationInteract.subscribe(this);
                inConversation = true;

                inputCounter++;
                return InteractiveComponentReturns.INCOMPLETE;
            } else {
                // Error handling for invalid private chat recipient selection
                invalidInputBoundChoice = true;
            }
            return InteractiveComponentReturns.INCOMPLETE;
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }






    @Override
    public String getKeyword() {
        return "";
    }

    /**
     * Prints the current state of the chat interface based on the input counter.
     *
     * <p>
     * The method handles three main states of the chat:
     * </p>
     * <ul>
     * <li>Initial choice between public and private chat (inputCounter == 0)</li>
     * <li>Displaying either the public chat messages or the list of users for private chat (inputCounter == 1)</li>
     * <li>Displaying the private chat messages with a chosen user (inputCounter == 2 and choice == 2)</li>
     * </ul>
     *
     * <p>
     * In the initial state, the user is prompted to choose between public and private chat.
     * In the second state, the public chat messages are displayed if the choice is public chat,
     * or the list of users is displayed for selecting a private chat recipient if the choice is private chat.
     * In the third state, the private chat messages with the chosen user are displayed.
     * </p>
     *
     * <p>
     * Debug information is printed in the private chat state to verify the correct retrieval of private messages.
     * </p>
     */
    @Override
    public void print() {
        //This print is executed if inside a converation
        if(inConversation){
            //Print page header
            if (choice == 1) {
                System.out.println("\nPUBLIC CHAT");
            } else
                System.out.println("\nPRIVATE CHAT");

            //Print chat contents
            conversationView.print();

            if(emptyMessage) {
                System.out.println("You cannot send an empty message!");
                emptyMessage = false;
            }

            //Print page footer
            if (choice == 1) {
                System.out.println("\nPlease type your public message, to send it press ENTER");
            } else
                System.out.println("\nPlease type your private message, to send it press ENTER");
            return;
        }

        //This print is executed if still has to join any conversation
        if (inputCounter == 0) {
            if(invalidBinaryChoice) {
                System.out.println("You provided an invalid input, please select 1 or 2.");
                invalidBinaryChoice = false;
            }

            System.out.println("\n" + """ 
            Enter your choice:
             1 - Public Chat
             2 - Private Chat""");
        }


        else if (inputCounter == 1 && choice == 2) {
            System.out.println("\nSelect a player username to send a private message to by inserting an integer:");
            int i = 1;
            // Display the list of players for private messaging
            for (String username : recipients) {
                String usernameColorPrint = null;
                if (!username.equals(MyPlayer.getInstance().getUsername())) {
                    switch (lobbyUsers.getLobbyUserColors(username)) {
                        case GREEN -> usernameColorPrint = TerminalColor.GREEN;
                        case YELLOW -> usernameColorPrint = TerminalColor.YELLOW;
                        case BLUE -> usernameColorPrint = TerminalColor.BLUE;
                        case RED -> usernameColorPrint = TerminalColor.RED;
                    }
                    System.out.println(i++ + " - Player username: " + usernameColorPrint + username + TerminalColor.RESET);
                }
            }
            if(invalidInputBoundChoice) {
                System.out.println("You provided and invalid input, please select one of the players on screen");
                invalidInputBoundChoice = false;
            }
        }
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public void update() {
        //todo call print from state
    }
}
