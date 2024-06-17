package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.Chat;
import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ChatMessageSender;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatState extends ViewState{
    List<Component> passiveComponents;
    InteractiveComponent mainComponent;

    ViewState previousState;

    private final Logger logger;

    public ChatState(ClientModel model, ViewState previousState) {
        super(model);
        logger = Logger.getLogger(WaitState.class.getName());

        Chat.getInstance().resetNewMessages();

        this.previousState = previousState;
        previousState.sleepOnObservables();

        passiveComponents = new ArrayList<>();
        passiveComponents.add(new WaitTypeView(this));

        mainComponent = new ChatMessageSender(this);

        print();
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayChatState();

        mainComponent.print();
    }

    @Override
    public boolean handleInput(String input) {
        if(input.equalsIgnoreCase("quitChat")) {
            nextState();
        }
        else
            mainComponent.handleInput(input);
        return true;
    }

    @Override
    public void update() {
        print();
    }

    private void nextState() {
        model.unsubscribe(this);
        sleepOnObservables();

        model.setView(previousState);
        previousState.wakeUpOnObservables();
        previousState.print();
    }
}