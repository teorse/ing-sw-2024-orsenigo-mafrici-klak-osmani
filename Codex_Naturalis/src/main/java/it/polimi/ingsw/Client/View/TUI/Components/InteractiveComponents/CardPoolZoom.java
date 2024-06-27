package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.CardPools;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.TUI.Components.CardPoolView;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.Server.Model.Game.Table.CardPoolTypes;

/**
 * Represents an interactive component for zooming into either the Resource Pool or Golden Pool of cards.
 * This component handles user input to select between these two card pools and displays the corresponding
 * pool view once a selection is made. It manages input validation, error handling for invalid inputs,
 * and updates its state based on user interaction.
 */

public class CardPoolZoom extends InteractiveComponent{
    private CardPoolTypes choice;
    boolean invalidInput;

    /**
     * Initializes a CardPoolZoom interactive component with an input counter limit of 1.
     * Sets up observation for changes in the CardPools instance and initializes attributes.
     * The component allows the user to select between the Resource Pool and the Golden Pool
     * of cards and displays the corresponding pool view based on the user's choice.
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "cardpoolzoom";
    }

    /**
     * Retrieves the description associated with the CardPoolZoom interactive component.
     * This method returns an empty string as there is no specific description for this component.
     *
     * @return An empty string.
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * Removes this instance from the list of observers for changes in CardPools.
     * This method ensures that the CardPoolZoom component no longer receives updates
     * about changes in the CardPools instance.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, CardPools.getInstance());
    }

    /**
     * Adds this instance to the list of observers for changes in CardPools.
     * This method ensures that the CardPoolZoom component receives updates
     * about changes in the CardPools instance.
     */
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
