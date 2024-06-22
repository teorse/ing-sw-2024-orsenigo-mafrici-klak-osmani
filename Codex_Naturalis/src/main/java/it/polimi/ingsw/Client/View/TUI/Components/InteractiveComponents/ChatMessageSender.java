package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;


import it.polimi.ingsw.Client.Model.*;
import it.polimi.ingsw.Client.Network.ClientConnector;
import it.polimi.ingsw.Client.View.TUI.Components.ChatConversationView;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPSendChatMessage;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatMessageSender extends InteractiveComponent{
    //ATTRIBUTES
    private final Chat chat;
    private int choice;
    private String chosenUser;
    private final ClientConnector connection;
    private LobbyUsers lobbyUsers;

    private final List<String> recipients;

    private ChatConversationView conversationView;
    private ChatMessagesStack conversationInteract;
    private boolean inConversation;

    private final List<Observable> observables;

    //ERROR PRINTING VARIABLES
    private boolean emptyMessage = false;
    private boolean invalidBinaryChoice = false;
    private boolean invalidInputBoundChoice = false;

    private final Logger logger;




    //CONSTRUCTOR
    /**
     * Constructs a ChatMessageSender object, initializing it with specific attributes
     * and settings related to chat messaging functionality.
     *
     * Initializes the object by calling the superclass constructor with a value of 1.
     * Sets up the Chat instance and adds it to the list of observables for RefreshManager.
     * Retrieves the lobby users and sets up the connection to the client model's client connector.
     * Initializes the object with a flag indicating it is not in conversation.
     * Sets up the recipients list by iterating through lobby users and excluding the current player.
     */
    public ChatMessageSender() {
        super(1);
        logger = Logger.getLogger(ChatMessageSender.class.getName());
        logger.info("Initializing chat state.");

        // Initialize Chat instance
        chat = Chat.getInstance();
        observables = new ArrayList<>();
        observables.add(chat);

        // Add Chat instance to RefreshManager observables
        RefreshManager.getInstance().addObserved(this, chat);

        // Retrieve lobby users and initialize connection to client model's client connector
        lobbyUsers = LobbyUsers.getInstance();
        connection = ClientModel.getInstance().getClientConnector();

        // Initialize conversation state
        inConversation = false;

        // Initialize recipients list excluding the current player
        recipients = new ArrayList<>();
        for (int i = 0; i < lobbyUsers.size(); i++) {
            String username = lobbyUsers.getLobbyUserNameByIndex(i);
            if (!username.equals(MyPlayer.getInstance().getUsername())) {
                recipients.add(username);
            }
        }
    }






    //METHODS
    /**
     * Handles user input for the chat system. Manages the state of the input handling process,
     * including public and private chat interactions, and transitions between different input states.
     *
     * @param input the user input string to be processed
     * @return the state of the input handling process as an InteractiveComponentReturns enum
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {

        // Process input through superclass method
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if (superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        // Handle in-conversation input
        if (inConversation) {
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

        // Get current input counter state
        int inputCounter = getInputCounter();
        if (inputCounter == 0) {
            // Handle initial choice input (Public or Private Chat)
            if (InputValidator.validBinaryChoice(input)) {
                choice = Integer.parseInt(input);
                if (choice == 1) {

                    // Remove previous chat observable and refresh manager references
                    observables.remove(chat);
                    RefreshManager.getInstance().removeObserved(this, chat);

                    // Set up public chat conversation view and interaction
                    conversationView = new ChatConversationView(chat.getPublicChat());
                    conversationInteract = chat.getPublicChat();

                    // Add new chat observable and refresh manager references
                    observables.add(conversationInteract);
                    RefreshManager.getInstance().addObserved(this, conversationInteract);

                    inConversation = true;
                    incrementInputCounter();
                    return InteractiveComponentReturns.INCOMPLETE;
                }
                incrementInputCounter();
            } else {
                // Error handling for invalid initial choice input
                invalidBinaryChoice = true;
            }
            return InteractiveComponentReturns.INCOMPLETE;
        } else if (inputCounter == 1 && choice == 2) {
            // Handle selection of a user for private chat
            if (InputValidator.checkInputBound(input, 1, lobbyUsers.size() - 1)) {
                chosenUser = recipients.get(Integer.parseInt(input) - 1);

                // Remove previous chat observable and refresh manager references
                observables.remove(chat);
                RefreshManager.getInstance().removeObserved(this, chat);

                // Set up private chat conversation view and interaction
                conversationView = new ChatConversationView(chat.getPrivateChat(chosenUser));
                conversationInteract = chat.getPrivateChat(chosenUser);

                // Add new chat observable and refresh manager references
                observables.add(conversationInteract);
                RefreshManager.getInstance().addObserved(this, conversationInteract);

                inConversation = true;

                incrementInputCounter();
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
        return "chat";
    }

    @Override
    public String getDescription() {
        return "/exit -> to exit the chat state";
    }

    @Override
    public void cleanObserved() {
        for(Observable observable : observables){
            RefreshManager.getInstance().removeObserved(this, observable);
        }
    }

    @Override
    public void refreshObserved() {
        for(Observable observable : observables){
            RefreshManager.getInstance().addObserved(this, observable);
        }
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
        logger.fine("Printing Chat State.");
        logger.fine("Input counter is: "+getInputCounter());
        logger.fine("inConversation is: "+inConversation);

        if(getInputCounter() == 0 || (getInputCounter() == 1 && choice == 2))
            inConversation = false;

        //This print is executed if inside a conversation
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
        int inputCounter = getInputCounter();
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

        super.print();
    }
}
