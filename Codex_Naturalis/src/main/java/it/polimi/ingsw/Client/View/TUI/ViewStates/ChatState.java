package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ChatMessageSender;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatState extends ViewState {
    List<Component> passiveComponenets;
    InteractiveComponent mainComponent;

    ViewState previousState;

    private final Logger logger;

    public ChatState(ClientModel2 model, ViewState previousState) {
        super(model);
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponenets = new ArrayList<>();
        passiveComponenets.add(new WaitTypeView());

        mainComponent = new ChatMessageSender();

        this.previousState = previousState;
    }

    @Override
    public void print() {
        TextUI.clearCMD();
        TextUI.displayChatState();

        mainComponent.print();
    }

    @Override
    public void handleInput(String input) {
        //TODO implement exit and back command

        mainComponent.handleInput(input);
    }

    @Override
    public void update() {

    }

    private void nextState(){
        model.setView(previousState);
        previousState.update();
    }
}
