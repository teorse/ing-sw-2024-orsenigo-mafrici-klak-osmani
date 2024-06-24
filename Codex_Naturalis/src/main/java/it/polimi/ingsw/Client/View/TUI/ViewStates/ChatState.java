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

/**
 * Represents the chat state in the TUI, allowing users to send and view chat messages.
 */
public class ChatState extends InteractiveState {
    // List of passive components to display
    private final List<Component> passiveComponents;

    // Previous state before entering the chat
    ViewState previousState;

    private final Logger logger;
    private boolean quitChat;

    /**
     * Constructor for ChatState.
     * @param previousState the state that was active before entering the chat
     */
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

    /**
     * Prints the chat state, clearing the CMD and displaying the chat components.
     */
    @Override
    public void print() {
        synchronized (printLock) {
            TextUI.clearCMD();
            TextUI.displayChatState();
            getMainComponent().print();
        }
    }

    /**
     * Handles the input from the user. If the input is "/quitChat", it will quit the chat.
     * Otherwise, it will handle the input as a chat message.
     * @param input the user input
     * @return true if the input was handled
     */
    @Override
    public boolean handleInput(String input) {
        if (input.equalsIgnoreCase("/quitChat")) {
            quitChat = true;
            nextState();
        } else {
            super.handleInput(input);
            ClientModel.getInstance().printView();
        }
        return true;
    }

    /**
     * Refreshes the observables, currently does nothing in this state.
     */
    @Override
    public void refreshObservables() {
        // No specific observables to refresh in ChatState
    }

    /**
     * Updates the state, checking if a state transition is necessary.
     */
    @Override
    public void update() {
        logger.fine("Updating in ChatState");
        if (!nextState())
            ClientModel.getInstance().printView();
        logger.fine("Finished updating in ChatState");
    }

    /**
     * Checks and transitions to the next state if necessary.
     * @return true if a state transition occurred
     */
    boolean nextState() {
        synchronized (nextStateLock) {
            if (ClientModel.getInstance().getView().equals(this)) {

                if (super.nextState())
                    return true;

                if (quitChat) {
                    Chat.getInstance().resetNewMessages();

                    RefreshManager.getInstance().resetObservables();
                    ClientModel.getInstance().setView(previousState);
                    previousState.refreshObservables();
                    ClientModel.getInstance().printView();

                    return true;
                }
                return false;
            }
            return true;
        }
    }
}