package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Chat;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class ChatNotification extends LiveComponent {
    private final Chat chat;


    public ChatNotification(ViewState view){
        super(view);
        chat = Chat.getInstance();
        view.addObserved(chat);
    }

    @Override
    public void print() {
        if(Chat.getInstance().isNewMessages()){
            System.out.println(" (NEW MESSAGE)");
        }
    }

    @Override
    public void cleanObserved() {
        view.removeObserved(chat);
    }
}
