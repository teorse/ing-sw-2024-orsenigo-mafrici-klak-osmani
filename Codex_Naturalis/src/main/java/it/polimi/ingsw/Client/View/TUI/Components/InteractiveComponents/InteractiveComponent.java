package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.Component;

public abstract class InteractiveComponent extends Component {
    //ATTRIBUTES
    int inputCounter = 0;





    //METHODS
    //TODO fix return of the boolean
    public InteractiveComponentReturns handleInput(String input){

        if(input.equalsIgnoreCase("BACK")) {
            if(inputCounter > 0){
                inputCounter--;
                return InteractiveComponentReturns.INCOMPLETE;
            }
            else if(inputCounter == 0){
                return InteractiveComponentReturns.QUIT;
            }
        }
        //todo add logger because code should not reach this
        return InteractiveComponentReturns.INCOMPLETE;
    }


    public abstract String getKeyword();
}
