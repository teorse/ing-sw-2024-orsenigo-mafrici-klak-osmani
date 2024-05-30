package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

public class Zoomer extends InteractiveComponent {
    //ATTRIBUTES
    private InteractiveComponent subComponent;


    //METHODS
    @Override
    public boolean handleInput(String input) {
        if(inputCounter == 0){
            if (TextUI.checkInputBound(input, 1, 4)) {
                // Parse choice
                int choice = Integer.parseInt(input);
                if (choice == 1) {
                    subComponent = new CardMapZoom();
                    inputCounter++;
                } else if (choice == 2) {
                    new CardsHeldView().print();
                    inputCounter = 0;
                    return true;
                } else if (choice == 3) {
                    subComponent = new CardPoolZoom();
                    inputCounter++;
                }
                return false;
            }
            return false;
        }
        if(inputCounter == 1) {
            //returns true if the subComponent has finished its interaction cycle
            //returns false if the user has still to complete all interactions in the subComponent
            boolean subcomponentResult = subComponent.handleInput(input);
            if(subcomponentResult)
                inputCounter = 0;

            return subcomponentResult;
        }
        return false;
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
