package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.Chat;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;

/**
 * The ChatNotification class is responsible for displaying a notification in the terminal
 * when there are new chat messages available.
 * It observes the Chat model for changes and prints a notification if new messages are detected.
 */
public class ChatNotification extends LiveComponent {

    /**
     * Constructs a new ChatNotification component.
     * Initializes and refreshes its observation of the Chat model.
     */
    public ChatNotification() {
        super();
        refreshObserved();
    }

    /**
     * Prints a notification in the terminal if there are new chat messages available.
     * The notification is printed in green color to highlight its importance.
     */
    @Override
    public void print() {
        if (Chat.getInstance().isNewMessages()) {
            System.out.println(TerminalColor.GREEN + "\nNEW MESSAGES!" + TerminalColor.RESET);
        }
    }

    /**
     * Cleans up the observation of the Chat model when this component is no longer needed.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Chat.getInstance());
    }

    /**
     * Refreshes the observation of the Chat model.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Chat.getInstance());
    }
}
