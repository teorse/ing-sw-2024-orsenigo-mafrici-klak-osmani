package it.polimi.ingsw.Client.Model.States;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPSendChatMessage;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.logging.Logger;

public class ChatState extends ClientState{
    private final ClientState previousState;
    private int choice;
    private String chosenUser;
    private final Logger logger;

    /**
     * Constructs a new ClientState with the specified client model.
     * <p>
     * Initializes the ClientState with the provided client model, initializes the TextUI for interaction,
     * retrieves the player record of the current user, retrieves the lobby user record of the current user,
     * sets the input counter to 0, and initializes the operation success flag to false in the client model.
     *
     * @param model the client model
     */
    public ChatState(ClientModel model, ClientState previousState) {
        super(model);
        this.previousState = previousState;
        model.setChatState(true);
        model.setNewMessage(false);

        logger = Logger.getLogger(ConnectionState.class.getName());
        logger.info("Initializing ChatState");

        print();

        logger.fine("ChatState initialized");
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
    public void print() {
        if (inputCounter == 0) {
            TextUI.clearCMD();
            TextUI.displayChatState();

            // Instructions for going back or exiting the chat state
            System.out.println("If you want to go back at the previous choice, type BACK. If you want to exit the Chat State, type EXIT.");
            System.out.println("\n" + """ 
            Enter your choice:
             1 - Public Chat
             2 - Private Chat""");
        } else if (inputCounter == 1) {
            TextUI.clearCMD();
            TextUI.displayChatState();

            // Instructions for going back or exiting the chat state
            System.out.println("If you want to go back at the previous choice, type BACK. If you want to exit the Chat State, type EXIT.");
            if (choice == 1) {
                System.out.println("\nPUBLIC CHAT");
                // Display public chat messages
                for (int i = 0; i < model.getPublicChatMessages().size(); i++) {
                    System.out.println(displayMessage(model.getPublicChatMessages().get(i)));
                }
                System.out.println("\nPlease type your public message, to send it press ENTER");
            } else if (choice == 2) {
                System.out.println("\nSelect a player username to send a private message to by inserting an integer:");
                int i = 1;
                // Display the list of players for private messaging
                for (LobbyUserRecord lobbyUserRecord : model.getLobbyUserRecords()) {
                    String usernameColorPrint = null;
                    if (!lobbyUserRecord.username().equals(model.getMyUsername())) {
                        switch (lobbyUserRecord.color()) {
                            case GREEN -> usernameColorPrint = TerminalColor.GREEN;
                            case YELLOW -> usernameColorPrint = TerminalColor.YELLOW;
                            case BLUE -> usernameColorPrint = TerminalColor.BLUE;
                            case RED -> usernameColorPrint = TerminalColor.RED;
                        }
                        System.out.println(i++ + " - Player username: " + usernameColorPrint + lobbyUserRecord.username() + TerminalColor.RESET);
                    }
                }
            }
        } else if (inputCounter == 2 && choice == 2) {
            TextUI.clearCMD();
            TextUI.displayChatState();

            // Instructions for going back or exiting the chat state
            System.out.println("If you want to go back at the previous choice, type BACK. If you want to exit the Chat State, type EXIT.");
            System.out.println("\nPRIVATE CHAT");

            if (model.getPrivateChatMessages().containsKey(chosenUser)) {
                // Display private chat messages for the chosen user
                for (int i = 0; i < model.getPrivateChatMessages().get(chosenUser).size(); i++) {
                    System.out.println(displayMessage(model.getPrivateChatMessages().get(chosenUser).get(i)));
                }
            } else {
                System.out.println("No private messages found for user " + chosenUser);
            }
            System.out.println("\nPlease type your private message, to send it press ENTER");
        }
    }



    /**
     * Handles user input in the chat interface, managing different states such as public and private chat.
     * The method also handles navigation commands like "BACK" and "EXIT".
     *
     * @param input The user input as a string.
     */
    @Override
    public void handleInput(String input) {
        if (input.equalsIgnoreCase("BACK")) {
            // Handle "BACK" input: decrease inputCounter or reset choice based on current state
            if (inputCounter == 1) {
                choice = 0;
                inputCounter--;
            } else if (inputCounter == 2) {
                inputCounter--;
            }
            print();
        } else if (input.equalsIgnoreCase("EXIT")) {
            // Handle "EXIT" input: reset inputCounter and choice, then move to the next state
            inputCounter = 0;
            choice = 0;
            nextState();
        } else if (inputCounter == 0) {
            // Handle initial choice input (Public or Private Chat)
            if (TextUI.validBinaryChoice(input)) {
                inputCounter++;
                choice = Integer.parseInt(input);
            }
            print();
        } else if (inputCounter == 1) {
            if (choice == 1) {
                // Handle public chat message input
                if (!input.trim().isEmpty())
                    model.getClientConnector().sendPacket(new CSPSendChatMessage(new ChatMessageRecord(input.replaceFirst("^\\s+", ""))));
                else
                    System.out.println("You cannot send an empty message!");
                print();
            } else if (choice == 2) {
                // Handle selection of a user for private chat
                if (TextUI.checkInputBound(input, 1, model.getLobbyUserRecords().size() - 1)) {
                    chosenUser = model.getLobbyUserRecords().get(Integer.parseInt(input) - 1).username();
                    if (chosenUser.equals(model.getMyUsername())) {
                        chosenUser = model.getLobbyUserRecords().get(Integer.parseInt(input)).username();
                    }
                    inputCounter++;
                }
                print();
            }
        } else if (inputCounter == 2 && choice == 2) {
            // Handle private chat message input
            if (!input.trim().isEmpty())
                model.getClientConnector().sendPacket(new CSPSendChatMessage(new ChatMessageRecord(input.replaceFirst("^\\s+", ""), chosenUser)));
            else
                System.out.println("You cannot send an empty message!");
            print();
        }
    }

    @Override
    public void nextState() {
        model.setChatState(false);
        model.setClientState(previousState);
        previousState.nextState();

    }

    /**
     * This method formats and returns a chat message with the appropriate color based on the sender's username.
     *
     * @param messageRecord The chat message record to display.
     * @return A formatted string representing the chat message.
     */
    public String displayMessage(ChatMessageRecord messageRecord) {
        String usernameColorPrint = null;
        // Determine the color based on the sender's username
        switch (model.getLobbyUserColors(messageRecord.getSender())) {
            case LobbyUserColors.GREEN -> usernameColorPrint = TerminalColor.GREEN;
            case LobbyUserColors.YELLOW -> usernameColorPrint = TerminalColor.YELLOW;
            case LobbyUserColors.BLUE -> usernameColorPrint = TerminalColor.BLUE;
            case LobbyUserColors.RED -> usernameColorPrint = TerminalColor.RED;
        }
        // Return the formatted message string
        return messageRecord.getTimestamp() + " - " + usernameColorPrint + messageRecord.getSender() + TerminalColor.RESET + ": " + messageRecord.getMessage();
    }
}
