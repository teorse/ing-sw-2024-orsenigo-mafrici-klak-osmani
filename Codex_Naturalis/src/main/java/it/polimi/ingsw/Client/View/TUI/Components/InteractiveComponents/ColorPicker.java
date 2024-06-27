package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Lobby;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPChangeColor;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.List;

/**
 * Represents an interactive component for handling user color selection in the lobby.
 * <p>
 * This class allows users to change their color within the lobby interface. It manages user input
 * to select from a list of available colors and sends a corresponding packet to the server for color
 * change. It also handles input validation and state transitions based on user interaction.
 * </p>
 * <p>
 * The class extends InteractiveComponent and implements methods to handle input, print the current
 * state of available colors, and manage observed state for automatic updates via RefreshManager.
 * </p>
 */
public class ColorPicker extends InteractiveComponent{
    //ATTRIBUTES
    private final ClientModel model;
    private List<LobbyUserColors> availableUserColors;

    private boolean invalidChoice;





    //CONSTRUCTOR
    /**
     * Constructs a ColorPicker object to facilitate user color selection within the lobby interface.
     * <p>
     * Initializes the object by calling the superclass constructor with an input counter of 0.
     * Retrieves the singleton instance of ClientModel to access client-related functionalities.
     * Sets up observation of the Lobby instance for automatic updates via RefreshManager.
     * Initializes the object with a flag indicating no invalid choices made initially.
     * </p>
     */
    public ColorPicker(){
        super(0);
        this.model = ClientModel.getInstance();

        refreshObserved();

        invalidChoice = false;
    }





    //METHODS
    /**
     * Handles user input for changing user color in the lobby. Processes the input to ensure it is within
     * valid bounds and sends a packet to change the user color. Also manages the state transitions based on
     * the result of the input handling.
     *
     * @param input the user input string to be processed
     * @return the state of the input handling process as an InteractiveComponentReturns enum
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {

        // Process input through superclass method
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if (superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        // Validate the input against available user colors
        if (InputValidator.checkInputBound(input, 1, availableUserColors.size())) {
            // Send packet to change user color based on the validated input
            model.getClientConnector().sendPacket(new CSPChangeColor(Lobby.getInstance().getAvailableUserColors().get(Integer.parseInt(input) - 1)));
            return InteractiveComponentReturns.COMPLETE;
        } else {
            // Handle invalid input
            invalidChoice = true;
            return InteractiveComponentReturns.INCOMPLETE;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "color";
    }

    /**
     * Retrieves the description of this interactive component.
     *
     * @return A string describing the usage of the "/color" command to change the user's color.
     */
    @Override
    public String getDescription() {
        return "/color -> to change your user color";
    }

    /**
     * Prints the current state of the color picker interface based on available user colors and user input.
     * Displays a list of available colors and handles error messages for invalid user inputs.
     * If no colors are available, it informs the user accordingly.
     */
    @Override
    public void print() {
        availableUserColors = Lobby.getInstance().getAvailableUserColors();

        if(invalidChoice){
            invalidChoice = false;
            System.out.println("\nInvalid input. Type a number between 1 and " + availableUserColors.size());
        }

        if (availableUserColors != null && !availableUserColors.isEmpty()) {
            System.out.println("\nAvailable colors: ");
            int i = 1;
            for (LobbyUserColors color : availableUserColors) {
                System.out.println(i++ + " - " + color.name());
            }
            System.out.println("\nIf you want to change the color type the one you want by typing its number.");
        }
        else
            System.out.println("No colors available!");
    }

    /**
     * Removes this ColorPicker instance from observing changes in the Lobby instance.
     * This method ensures that the ColorPicker stops receiving updates from the Lobby when no longer needed.
     */
    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Lobby.getInstance());
    }

    /**
     * Adds this ColorPicker instance to observe changes in the Lobby instance.
     * This method ensures that the ColorPicker receives updates from the Lobby to reflect changes in available user colors.
     */
    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Lobby.getInstance());
    }
}
