package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.LiveComponent;

public abstract class InteractiveComponent extends LiveComponent {
    //ATTRIBUTES
    int inputCounter = 0;

    public InteractiveComponent() {
        super();
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
}
