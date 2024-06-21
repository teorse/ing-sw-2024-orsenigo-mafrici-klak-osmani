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
        //todo add logger because code should not reach this
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
}
