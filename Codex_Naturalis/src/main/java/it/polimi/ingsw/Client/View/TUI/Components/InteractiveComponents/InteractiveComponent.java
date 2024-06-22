package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;

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
        // todo add logger because code should not reach this
        return InteractiveComponentReturns.INCOMPLETE;
    }

    public abstract String getKeyword();

    public abstract String getDescription();

    public int getInputCounter() {
        return inputCounter;
    }

    public int getMaxInputCounter() {
        return maxInputCounter;
    }

    void incrementInputCounter(){
        inputCounter++;
    }

    void decrementInputCounter(){
        if(inputCounter > 0)
            inputCounter--;
    }

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
