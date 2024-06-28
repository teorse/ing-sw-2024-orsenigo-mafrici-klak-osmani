package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;

/**
 * Abstract class representing an interactive component in a text-based user interface (TUI).
 * This class extends {@link LiveComponent} and manages user interactions involving multiple
 * stages of input. It includes methods for handling input navigation back to previous menus
 * or choices, managing input counters, and printing prompts for user interaction. Each subclass
 * must implement methods to return keywords and descriptions for identification and user guidance.
 * Subclasses define specific behaviors for handling user input and managing interaction flow,
 * such as selecting options, placing cards, or navigating menus. The {@link #handleInput(String)}
 * method processes user inputs based on the current interaction stage and returns appropriate
 * status codes to indicate completion, quitting, or incomplete input handling.
 */
public abstract class InteractiveComponent extends LiveComponent {
    //ATTRIBUTES
    private int inputCounter = 0;
    /**
     * Stores the maximum value of the input counter at which inputs will still be processed.<br>
     * While the inputCounter is <= than the maxInputCounter the inputs are still being processed by the component.</br>
     * Once the inputCounter is > than the maxInputCounter then any inputs are being ignored.</br>
     * This is useful in situations where after completing the interaction with a component, there may be some
     * delay before getting the response from the server and we don't expect inputs from the user.
     */
    private final int maxInputCounter;

    /**
     * Constructs an interactive component with a specified maximum input counter.
     * This counter determines the maximum stage of input processing the component will handle.
     * After reaching this maximum input stage, further inputs are ignored until the counter is reset.
     *
     * @param maxInputCounter The maximum value of the input counter at which inputs will still be processed.
     *                        Inputs beyond this counter are ignored until the counter is reset.
     */
    public InteractiveComponent(int maxInputCounter) {
        super();
        this.maxInputCounter = maxInputCounter;
    }


    //METHODS
    /**
     * Handles the user input for navigating back to the previous menu or choice.
     * If the input is "/BACK", it decrements the input counter if it's greater than zero,
     * indicating a successful navigation back, and returns {@link InteractiveComponentReturns#COMPLETE}.
     * If the input counter is zero and "/BACK" is entered, it returns {@link InteractiveComponentReturns#QUIT}
     * to signal the intent to quit or return to a higher-level menu.
     * If the input is not "/BACK", the method returns {@link InteractiveComponentReturns#INCOMPLETE}.
     *
     * @param input The user input to process.
     * @return {@link InteractiveComponentReturns#COMPLETE} if successfully navigated back,
     *         {@link InteractiveComponentReturns#QUIT} if at the top-level and intending to quit,
     *         {@link InteractiveComponentReturns#INCOMPLETE} otherwise.
     */
    public InteractiveComponentReturns handleInput(String input){
        if(input.equalsIgnoreCase("/BACK")) {
            if(inputCounter > 0){
                inputCounter--;
                return InteractiveComponentReturns.COMPLETE;
            }
            else if(inputCounter == 0){
                return InteractiveComponentReturns.QUIT;
            }
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    /**
     * Returns the keyword associated with this interactive component.
     * The keyword is used to determine which component should handle the input when there are multiple
     * interactive component in the same view state.
     *
     * @return The keyword associated with this interactive component.
     */
    public abstract String getKeyword();

    /**
     * Returns the description of this interactive component.
     * The description can be used to communicate to the user the purpose or behavior of the component.
     *
     * @return The description of this interactive component.
     */
    public abstract String getDescription();

    /**
     * Retrieves the current input counter of this interactive component.
     * The input counter tracks the current stage of user inputs being processed.
     *
     * @return The current input counter of this interactive component.
     */
    public int getInputCounter() {
        return inputCounter;
    }

    /**
     * Retrieves the maximum input counter value allowed for this interactive component.
     * Inputs are processed only up to this maximum counter value.
     *
     * @return The maximum input counter value allowed for this interactive component.
     */
    public int getMaxInputCounter() {
        return maxInputCounter;
    }

    /**
     * Increments the input counter by one.
     * This method is typically used to advance to the next input stage during user interaction.
     */
    void incrementInputCounter(){
        inputCounter++;
    }

    /**
     * Decrements the input counter by one, if it is greater than zero.
     * This method is used to move back to the previous input stage during user interaction.
     * If the input counter is already zero, it remains unchanged.
     */
    void decrementInputCounter(){
        if(inputCounter > 0)
            inputCounter--;
    }

    /**
     * Resets the input counter to zero.
     * This method is used to reset the input stage when starting a new interaction or after completing an interaction.
     */
    void resetInputCounter(){
        inputCounter = 0;
    }

    /**
     * Prints a message prompting the user to go back to the previous choice if applicable.
     * This message is printed only when the input counter is within the valid range (greater than 0 and less than or equal to maxInputCounter).
     * The message informs the user that they can type "/back" to return to the previous menu or choice.
     */
    @Override
    public void print() {
        if (inputCounter > 0 && inputCounter <= maxInputCounter) {
            System.out.println("\nIf you want to go back to the previous choice, type: /back");
        }
    }

}
