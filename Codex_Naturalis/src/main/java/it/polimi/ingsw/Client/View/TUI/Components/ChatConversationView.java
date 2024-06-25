package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ChatMessagesStack;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

/**
 * The ChatConversationView class is responsible for displaying the chat conversation.
 * It observes changes in the ChatMessagesStack model to update the view accordingly.
 */
public class ChatConversationView extends LiveComponent {
    private final ChatMessagesStack conversation;

    /**
     * Constructs a new ChatConversationView with the specified conversation.
     *
     * @param conversation the chat messages stack to be displayed
     */
    public ChatConversationView(ChatMessagesStack conversation) {
        super();
        this.conversation = conversation;
        refreshObserved();
    }

    /**
     * Prints the chat conversation.
     * If there are no messages in the chat, it prompts the user to send the first message.
     * Otherwise, it prints each chat message using the ChatMessageView class.
     */
    @Override
    public void print() {
        // Check if the conversation is empty
        if (conversation.size() == 0) {
            System.out.println("No messages in this chat yet, send the first message :) ");
        } else {
            // Loop through and print each message in the conversation
            for (int i = 0; i < conversation.size(); i++) {
                new ChatMessageView(conversation.get(i)).print();
            }
        }
    }

    /**
     * Cleans the observed objects by removing this view from the observers of the conversation.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, conversation);
    }

    /**
     * Refreshes the observed objects by adding this view to the observers of the conversation.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, conversation);
    }
}
