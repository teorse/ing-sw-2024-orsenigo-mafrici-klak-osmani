package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ChatMessagesStack;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class ChatConversationView extends Component implements Observer {
    private final ChatMessagesStack conversation;

    public ChatConversationView(ChatMessagesStack conversation, ViewState viewState) {
        this.conversation = conversation;
        conversation.subscribe(this);
    }

    @Override
    public void update() {
        //todo add call to print for the whole state
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
    public void cleanUp() {

    }
}
