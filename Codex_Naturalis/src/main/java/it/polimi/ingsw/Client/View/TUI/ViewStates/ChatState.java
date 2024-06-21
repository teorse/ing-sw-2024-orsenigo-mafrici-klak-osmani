package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.Chat;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ChatMessageSender;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatState extends InteractiveState {
    //TODO fix the extends to provide the available commands
    private final List<Component> passiveComponents;

    ViewState previousState;

    private final Logger logger;
    private boolean quitChat;

    public ChatState(ViewState previousState) {
        super(new ChatMessageSender());
        logger = Logger.getLogger(ChatState.class.getName());
        quitChat = false;

        Chat.getInstance().resetNewMessages();

        this.previousState = previousState;
        RefreshManager.getInstance().resetObservables();

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new WaitTypeView());
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayChatState();

        getMainComponent().print();
    }

    @Override
    public boolean handleInput(String input) {
        if(input.equalsIgnoreCase("quitChat")) {
            quitChat = true;
            nextState();
        }
        else
            getMainComponent().handleInput(input);
        return true;
    }

    @Override
    public void refreshObservables() {

    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }

    boolean nextState() {
        if(super.nextState())
            return true;

        if(quitChat) {
            RefreshManager.getInstance().resetObservables();
            ClientModel.getInstance().setView(previousState);
            previousState.refreshObservables();
            previousState.print();

            return true;
        }

        return false;
    }
}