package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Chat;
import it.polimi.ingsw.Client.View.Observer;

public class ChatNotification extends Component implements Observer {
    private final Chat chat;
    private boolean newMessage;


    public ChatNotification(){
        chat = Chat.getInstance();
        chat.subscribe(this);
    }

    @Override
    public void update() {
      newMessage = true;
    }

    @Override
    public void print() {
        if(newMessage){
            System.out.println(" (NEW MESSAGE)");
        }
        newMessage = false;
    }
}
