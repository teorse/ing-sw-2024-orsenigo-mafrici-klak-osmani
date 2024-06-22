package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.Game;
import it.polimi.ingsw.Client.View.TUI.Components.CardsHeldView;
import it.polimi.ingsw.Client.View.InputValidator;

//todo further review zoomer class
public class Zoomer extends InteractiveComponent {
    //ATTRIBUTES
    private int choice;
    private InteractiveComponent subComponent;
    private boolean invalidInput;

    public Zoomer() {
        super(1);

        refreshObserved();

        invalidInput = false;
    }


    //METHODS
    /**
     * Handles user input for interacting with zoom options related to different components of the game.
     * Allows the user to select which component details to view and interacts with subcomponents accordingly.
     *
     * @param input The user input provided during interaction.
     * @return {@code InteractiveComponentReturns.COMPLETE} if the game setup is not finished;
     *         {@code InteractiveComponentReturns.QUIT} if the superclass interaction returns quit;
     *         {@code InteractiveComponentReturns.INCOMPLETE} if the superclass interaction is complete;
     *         otherwise, returns the result of interaction with subcomponents.
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        // If the game setup is not finished, complete the current component interaction
        if (!Game.getInstance().isSetupFinished())
            return InteractiveComponentReturns.COMPLETE;

        // Process input through superclass method
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if (superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        int inputCounter = getInputCounter();

        // Handle the first stage of input
        if (inputCounter == 0) {
            if (InputValidator.checkInputBound(input, 1, 3)) {
                // Parse choice and initialize subcomponents based on the choice
                choice = Integer.parseInt(input);
                if (choice == 1) {
                    subComponent = new CardMapZoom();
                    incrementInputCounter();
                } else if (choice == 2) {
                    incrementInputCounter();
                } else if (choice == 3) {
                    subComponent = new CardPoolZoom();
                    incrementInputCounter();
                }
            } else {
                invalidInput = true;
            }

            return InteractiveComponentReturns.INCOMPLETE;
        }

        // Handle the interaction with subcomponents
        if (inputCounter == 1) {
            // Process input through the subcomponent and manage the result
            InteractiveComponentReturns subcomponentResult = subComponent.handleInput(input);
            if (subcomponentResult.equals(InteractiveComponentReturns.QUIT))
                decrementInputCounter();

            return subcomponentResult;
        }

        // This code should not be reached; log an error if it does
        // todo add logger, program should not reach this code
        return InteractiveComponentReturns.INCOMPLETE;
    }



    @Override
    public String getKeyword() {
        return "zoom";
    }

    @Override
    public String getDescription() {
        return "/zoom -> to zoom a card map, cards held or card pool";
    }

    /**
     * Prints the current state of the view to the console. This method handles the display of different options
     * based on the input counter and the user's choice. It provides instructions for navigating the zoom options
     * and prints relevant details for each option.
     */
    @Override
    public void print() {

        // Check if the game setup is finished
        if (!Game.getInstance().isSetupFinished())
            return;

        int inputCounter = getInputCounter();

        // Display initial zoom options
        if (inputCounter == 0) {
            System.out.println("\n" + """
                Enter what do you want to zoom:
                 1 - CardMap details
                 2 - CardHeld
                 3 - CardPool""");
        }

        // Display the selected option's details or navigate back
        else if (inputCounter == 1) {
            if (choice == 2) {
                new CardsHeldView().print();
            } else {
                subComponent.print();
            }

            // Provide navigation option to go back
            if (subComponent.getInputCounter() == 0)
                System.out.println("\nType /back to go to the previous menu");
        }

        // Handle invalid input case
        if (invalidInput) {
            invalidInput = false;
            System.out.println("The number provided is not a valid input.\nPlease type a number between 1 and 3.");
        }
    }

    @Override
    public void cleanObserved() {
        if(subComponent != null)
            subComponent.cleanObserved();
    }

    @Override
    public void refreshObserved() {
        if(subComponent != null)
            subComponent.refreshObserved();
    }
}
