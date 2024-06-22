package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;

public class LobbyChooser extends InteractiveComponent{

    private InteractiveComponent subComponent;

    private boolean wrongBinaryChoice;

    public LobbyChooser() {
        super(1);
        refreshObserved();
    }

    /**
     * Handles user input for creating or joining a lobby.
     * This method processes the input, initializes the appropriate subcomponent based on user choice,
     * and manages state transitions based on the input handling result.
     *
     * @param input the user input string to be processed
     * @return the state of the input handling process as an InteractiveComponentReturns enum
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        int inputCounter = getInputCounter();

        // Handle initial input for choosing lobby creation or joining
        if (inputCounter == 0) {

            // Process input through superclass method
            InteractiveComponentReturns superReturn = super.handleInput(input);
            if (superReturn == InteractiveComponentReturns.QUIT)
                return superReturn;
            else if (superReturn == InteractiveComponentReturns.COMPLETE) {
                return InteractiveComponentReturns.INCOMPLETE;
            }

            // Validate binary choice input
            if (InputValidator.validBinaryChoice(input)) {
                // Initialize subcomponent based on user choice
                if (Integer.parseInt(input) == 1) {
                    subComponent = new LobbyCreator();
                } else if (Integer.parseInt(input) == 2) {
                    subComponent = new LobbyJoiner();
                }

                // Increment input counter
                incrementInputCounter();
            } else {
                // Handle invalid binary choice input
                wrongBinaryChoice = true;
            }

            return InteractiveComponentReturns.INCOMPLETE;
        }

        // Handle input for the chosen subcomponent
        if (inputCounter == 1) {
            InteractiveComponentReturns result = subComponent.handleInput(input);

            // Handle quit signal from subcomponent
            if (result.equals(InteractiveComponentReturns.QUIT)) {
                decrementInputCounter();
                subComponent.cleanObserved();
                return InteractiveComponentReturns.INCOMPLETE;
            } else if (result.equals(InteractiveComponentReturns.COMPLETE)) {
                subComponent.cleanObserved();
            }

            return result;
        }

        return InteractiveComponentReturns.INCOMPLETE;
    }


    @Override
    public String getKeyword() {
        return "chooselobby";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void print() {
        if (wrongBinaryChoice) {
            System.out.println("\nWrong binary choice. Please type 1 or 2.");
        }

        int inputCounter = getInputCounter();
        if(inputCounter == 0) {
            System.out.println("\n" +
                    """
                            Enter your choice:
                             1 - Create a lobby
                             2 - Join a lobby""");
        }

        else if(inputCounter == 1){
            if(subComponent.getInputCounter() == 0)
                System.out.println("\nType /back to go to the previous menu");
            subComponent.print();
        }
    }

    @Override
    public void cleanObserved() {}

    @Override
    public void refreshObserved() {}
}
