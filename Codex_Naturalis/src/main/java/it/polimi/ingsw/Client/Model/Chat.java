package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ChatMessageRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Chat class manages both public and private chat messages within a lobby.
 * It acts as an observable for observing changes in the chat messages.
 * It also observes changes in the lobby users to manage private chat messages dynamically.
 */
public class Chat extends Observable implements Observer {
    // SINGLETON PATTERN
    private static Chat INSTANCE;

    /**
     * Private constructor to ensure singleton pattern and initialize chat structures.
     */
    private Chat() {
        publicChatMessages = new ChatMessagesStack(ClientModelConstants.PublicMessageStackSize);
        privateChatMessages = new HashMap<>();

        LobbyUsers.getInstance().subscribe(this);
        update();
    }

    /**
     * Retrieves the singleton instance of the Chat class.
     *
     * @return The singleton instance of Chat.
     */
    public synchronized static Chat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Chat();
        }
        return INSTANCE;
    }

    // ATTRIBUTES
    private boolean newMessages;
    private ChatMessagesStack publicChatMessages;
    private Map<String, ChatMessagesStack> privateChatMessages;

    // GETTERS
    /**
     * Retrieves the stack of public chat messages.
     *
     * @return The ChatMessagesStack containing public chat messages.
     */
    public ChatMessagesStack getPublicChat() {
        return publicChatMessages;
    }

    /**
     * Retrieves the stack of private chat messages for a specific recipient.
     *
     * @param recipient The username of the recipient.
     * @return The ChatMessagesStack containing private chat messages for the recipient.
     */
    public ChatMessagesStack getPrivateChat(String recipient) {
        return privateChatMessages.get(recipient);
    }

    /**
     * Checks if there are new messages in the chat.
     *
     * @return `true` if there are new messages; `false` otherwise.
     */
    public boolean isNewMessages() {
        return newMessages;
    }

    // SETTERS
    /**
     * Sets the stack of public chat messages.
     *
     * @param publicChatMessages The ChatMessagesStack containing public chat messages to set.
     */
    public void setPublicChatMessages(ChatMessagesStack publicChatMessages) {
        this.publicChatMessages = publicChatMessages;
        super.updateObservers();
    }

    /**
     * Sets the map of private chat messages for all users.
     *
     * @param privateChatMessages The map containing private chat messages for all users.
     */
    public void setPrivateChatMessages(Map<String, ChatMessagesStack> privateChatMessages) {
        this.privateChatMessages = privateChatMessages;
        super.updateObservers();
    }

    /**
     * Sets the stack of private chat messages for a specific recipient.
     *
     * @param username           The username of the recipient.
     * @param privateChatMessages The ChatMessagesStack containing private chat messages to set.
     */
    public void setSpecificPrivateChatMessages(String username, ChatMessagesStack privateChatMessages) {
        this.privateChatMessages.put(username, privateChatMessages);
        super.updateObservers();
    }

    /**
     * Resets the flag indicating new messages to `false`.
     */
    public void resetNewMessages() {
        newMessages = false;
    }

    // METHODS
    /**
     * Receives a chat message and stores it in the appropriate public or private stack.
     * Notifies observers of the new message.
     *
     * @param chatMessage The ChatMessageRecord representing the received chat message.
     */
    public void receiveChatMessage(ChatMessageRecord chatMessage) {
        if (chatMessage.isMessagePrivate()) {
            String sender = chatMessage.getSender();
            if (sender.equals(MyPlayer.getInstance().getUsername()))
                privateChatMessages.get(chatMessage.getRecipient()).add(chatMessage);
            else
                privateChatMessages.get(sender).add(chatMessage);
        } else {
            publicChatMessages.add(chatMessage);
        }

        newMessages = true;
        super.updateObservers();
    }

    /**
     * Updates the private chat messages map based on the current lobby users.
     * Removes obsolete private chat messages and initializes new entries as needed.
     */
    @Override
    public void update() {
        List<LobbyUserRecord> users = LobbyUsers.getInstance().getLobbyUserRecords();

        // Initialize private chat messages for new users
        for (LobbyUserRecord user : users) {
            if (!privateChatMessages.containsKey(user.username())) {
                privateChatMessages.put(user.username(), new ChatMessagesStack(ClientModelConstants.PrivateMessageStackSize));
            }
        }

        // Remove private chat messages for users no longer in the lobby
        for (String username : new ArrayList<>(privateChatMessages.keySet())) {
            List<String> newUsernames = users.stream().map(LobbyUserRecord::username).toList();
            if (!newUsernames.contains(username)) {
                privateChatMessages.remove(username);
            }
        }
    }
}
