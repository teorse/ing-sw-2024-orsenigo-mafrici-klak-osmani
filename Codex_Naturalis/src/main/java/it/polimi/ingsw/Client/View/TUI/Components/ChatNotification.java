package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Chat;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;

public class ChatNotification extends LiveComponent {


    public ChatNotification(){
        super();
        refreshObserved();
    }

    @Override
    public void print() {
        if(Chat.getInstance().isNewMessages()){
            System.out.println(TerminalColor.GREEN + "\nNEW MESSAGES!" + TerminalColor.RESET);
        }
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Chat.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Chat.getInstance());
    }
}
