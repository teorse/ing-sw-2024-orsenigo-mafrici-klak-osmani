package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ChatMessagesStack;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class ChatConversationView extends LiveComponent{
    private final ChatMessagesStack conversation;

    public ChatConversationView(ChatMessagesStack conversation) {
        super();
        this.conversation = conversation;
        refreshObserved();
    }

    @Override
    public void print() {

        if(conversation.size() == 0){
            System.out.println("No messages in this chat yet, send the first message :) ");
        }
        else {
            for (int i = 0; i < conversation.size(); i++)
                new ChatMessageView(conversation.get(i)).print();
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, conversation);
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, conversation);
    }
}
