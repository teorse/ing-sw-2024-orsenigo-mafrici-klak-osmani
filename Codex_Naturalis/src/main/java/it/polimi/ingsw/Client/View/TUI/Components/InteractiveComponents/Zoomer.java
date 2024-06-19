package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.CardsHeldView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;

//todo further review zoomer class
public class Zoomer extends InteractiveComponent {
    //ATTRIBUTES
    private int choice;
    private InteractiveComponent subComponent;
    private boolean invalidInput;

    public Zoomer() {
        super();

        refreshObserved();

        invalidInput = false;
    }


    //METHODS
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if(inputCounter == 0){
            if (InputValidator.checkInputBound(input, 1, 3)) {
                // Parse choice
                choice = Integer.parseInt(input);
                if (choice == 1) {
                    subComponent = new CardMapZoom();
                    inputCounter++;
                } else if (choice == 2) {
                    inputCounter++;
                } else if (choice == 3) {
                    subComponent = new CardPoolZoom();
                    inputCounter++;
                }
            }
            else
                invalidInput = true;

            return InteractiveComponentReturns.INCOMPLETE;
        }
        if(inputCounter == 1) {
            //returns true if the subComponent has finished its interaction cycle
            //returns false if the user has still to complete all interactions in the subComponent
            InteractiveComponentReturns subcomponentResult = subComponent.handleInput(input);
            if(subcomponentResult.equals(InteractiveComponentReturns.QUIT))
                inputCounter--;

            return subcomponentResult;
        }

        //todo add logger, program should not reach this code
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "zoom";
    }

    @Override
    public void print() {
        if(inputCounter == 0) {
            System.out.println("\n" + """
                    Enter what do you want to zoom:
                     1 - CardMap details
                     2 - CardHeld
                     3 - CardPool""");
        }

        else if(inputCounter == 1) {
            if(choice == 2){
                new CardsHeldView().print();
            }
            else
                subComponent.print();
        }

        if(invalidInput){
            invalidInput = false;
            System.out.println("The number provided is not a valid input.\nPlease type a number between 1 and 3.");
        }
    }

    @Override
    public void cleanObserved() {
        subComponent.cleanObserved();
    }

    @Override
    public void refreshObserved() {
        subComponent.refreshObserved();
    }
}
