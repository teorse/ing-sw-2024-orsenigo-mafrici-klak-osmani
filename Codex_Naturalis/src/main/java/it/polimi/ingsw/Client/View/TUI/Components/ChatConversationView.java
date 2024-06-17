package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ChatMessagesStack;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class ChatConversationView extends LiveComponent{
    private final ChatMessagesStack conversation;
    private final ViewState view;

    public ChatConversationView(ChatMessagesStack conversation, ViewState view) {
        super(view);
        this.view = view;
        this.conversation = conversation;
        view.addObserved(conversation);
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
        view.removeObserved(conversation);
    }
}
