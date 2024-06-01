package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Chat;
import it.polimi.ingsw.Client.Model.Observable;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

import java.util.List;

public class ChatNotification extends Component implements Observer {
    private final Chat chat;
    private boolean newMessage;


    public ChatNotification(ViewState viewState){
        super(viewState);
        chat = Chat.getInstance();
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
