package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardPools;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.CardPoolView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

public class CardPoolZoom extends InteractiveComponent{
    private CardPoolTypes choice;
    boolean invalidInput;


    public CardPoolZoom() {
        super(1);
        invalidInput = false;

        refreshObserved();
    }

    //METHODS
    /**
     * Handles user input for selecting between Resource Pool and Golden Pool of cards.
     * If no input counter is set, it checks the validity of the input and sets the choice
     * accordingly between Resource Pool (1) and Golden Pool (2).
     *
     * @param input The user input provided to select the pool type.
     * @return {@code InteractiveComponentReturns.COMPLETE} if the input is successfully handled,
     *         {@code InteractiveComponentReturns.INCOMPLETE} otherwise.
     */
    public InteractiveComponentReturns handleInput(String input) {
        // Call superclass method to handle input if necessary
        InteractiveComponentReturns superReturn = super.handleInput(input);

        // Process input if input counter is 0 (initial stage)
        if(getInputCounter() == 0) {
            // Check if superclass returned QUIT or COMPLETE
            if (superReturn == InteractiveComponentReturns.QUIT)
                return superReturn;
            else if (superReturn == InteractiveComponentReturns.COMPLETE) {
                return InteractiveComponentReturns.INCOMPLETE;
            }

            // Validate binary choice input (1 for RESOURCE, 2 for GOLDEN)
            if (InputValidator.validBinaryChoice(input)) {
                if (Integer.parseInt(input) == 1)
                    choice = CardPoolTypes.RESOURCE;
                else
                    choice = CardPoolTypes.GOLDEN;
                incrementInputCounter();
            } else {
                // Set invalid input flag if the input is not valid
                invalidInput = true;
            }
        }

        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "cardpoolzoom";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardPools.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, CardPools.getInstance());
    }

    /**
     * Prints the interface for zooming into either the Resource Pool or the Golden Pool of cards.
     * Displays options based on the current input counter and handles error messages for invalid inputs.
     * If the user has selected a pool type, it prints the corresponding pool view.
     */
    @Override
    public void print() {
        int inputCounter = getInputCounter();

        // Handle the first stage of input
        if(inputCounter == 0) {
            // Display error message for invalid input if set
            if(invalidInput)
                System.out.println("Invalid input. Please enter 1 or 2 to select the pool type!");

            // Print the initial prompt to select the pool type
            System.out.println("\n" + """
                Select the pool type to zoom:
                 1 - Resource Pool
                 2 - Golden Pool""");
        }
        // Handle the second stage of input
        else if(inputCounter == 1) {
            // Print the appropriate pool view based on the choice
            if(choice == CardPoolTypes.RESOURCE)
                new CardPoolView(CardPoolTypes.RESOURCE).print();
            else
                new CardPoolView(CardPoolTypes.GOLDEN).print();
        }

        // Call the superclass print method to handle any additional printing
        super.print();
    }
}
