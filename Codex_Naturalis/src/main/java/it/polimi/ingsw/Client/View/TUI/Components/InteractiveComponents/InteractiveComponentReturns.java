package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

/**
 * Enum representing the possible return statuses of interactive components in the Text User Interface (TUI).
 */
public enum InteractiveComponentReturns {

    /**
     * Indicates that the interactive component has completed its task successfully.
     */
    COMPLETE,

    /**
     * Indicates that the interactive component has not completed its task and may need further action.
     */
    INCOMPLETE,

    /**
     * Indicates that the user has chosen to quit the interactive component.
     */
    QUIT
}
