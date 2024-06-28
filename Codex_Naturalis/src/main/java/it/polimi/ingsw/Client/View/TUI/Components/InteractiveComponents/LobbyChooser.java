package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;

/**
 * Represents an interactive component responsible for handling user input to either create or join a lobby.
 * This component manages the process of choosing between lobby creation and joining,
 * initializes the appropriate subcomponent based on user input, and delegates input handling to the subcomponent.
 * It manages state transitions and provides feedback to the user regarding the choices made.
 */
public class LobbyChooser extends InteractiveComponent{

    private InteractiveComponent subComponent;

    private boolean wrongBinaryChoice;

    /**
     * Constructs a LobbyChooser object, initializing it with specific settings and observing lobby-related changes.
     * The LobbyChooser component allows users to interactively choose between creating a new lobby or joining an existing one.
     * It initializes with an input counter set to 1 to manage the initial choice selection state.
     * Observes changes in the lobby state to reflect real-time updates in the user interface.
     */
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
        wrongBinaryChoice = false;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "chooselobby";
    }

    /**
     * Retrieves the description of the current state or functionality of the LobbyChooser component.
     *
     * @return an empty string, as the LobbyChooser component does not provide a specific description.
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * Prints the current state of the LobbyChooser component based on its internal state and user interactions.
     * Handles displaying messages for wrong binary choices and prompts for choosing between creating or joining a lobby.
     * If a subcomponent is initialized (like LobbyCreator or LobbyJoiner), it delegates printing to the subcomponent.
     */
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

    /**
     * Cleans up any observed resources or connections that the LobbyChooser component is monitoring.
     * Since LobbyChooser does not actively observe any resources, this method has no effect.
     */
    @Override
    public void cleanObserved() {}

    /**
     * Refreshes any observed resources or connections that the LobbyChooser component is monitoring.
     * Since LobbyChooser does not actively observe any resources, this method has no effect.
     */
    @Override
    public void refreshObserved() {}
}
