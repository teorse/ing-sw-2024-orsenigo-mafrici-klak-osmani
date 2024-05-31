package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.Components.CardsHeldView;
import it.polimi.ingsw.Client.View.TUI.TextUI;

public class Zoomer extends InteractiveComponent {
    //ATTRIBUTES
    private InteractiveComponent subComponent;



    //METHODS
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        if(inputCounter == 0){
            if(input.equalsIgnoreCase("BACK"))
                return InteractiveComponentReturns.QUIT;


            if (TextUI.checkInputBound(input, 1, 3)) {
                // Parse choice
                int choice = Integer.parseInt(input);
                if (choice == 1) {
                    subComponent = new CardMapZoom();
                    inputCounter++;
                } else if (choice == 2) {
                    //todo
                    new CardsHeldView().print();
                    inputCounter++;
                } else if (choice == 3) {
                    subComponent = new CardPoolZoom();
                    inputCounter++;
                }
            }
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
            subComponent.print();
        }
    }
}
