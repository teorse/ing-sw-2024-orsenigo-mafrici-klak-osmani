package it.polimi.ingsw.Client.View.TUI.ViewStates;

import it.polimi.ingsw.Client.Model.ClientModel2;
import it.polimi.ingsw.Client.View.TUI.Components.*;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.ChatMessageSender;
import it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents.InteractiveComponent;
import it.polimi.ingsw.Client.View.TUI.TextUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatState extends LobbyStates{
    List<Component> passiveComponenets;
    InteractiveComponent mainComponent;

    ViewState previousState;

    private final Logger logger;

    public ChatState(ClientModel2 model, ViewState previousState) {
        super(model);
        logger = Logger.getLogger(WaitState.class.getName());

        passiveComponenets = new ArrayList<>();

        mainComponent = new ChatMessageSender(this);

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

        if(input.equalsIgnoreCase("quitChat")) {

            nextState();
        }
        mainComponent.handleInput(input);
    }

    @Override
    public void update() {
        if(!nextState())
            print();
    }

    boolean nextState() {
        if(!super.nextState())

        if () {
            model.unsubscribe(this);
            //Component unsubscri
            model.setView(previousState);
            previousState.update();
            return true;
        } else
            return false;
    }
}