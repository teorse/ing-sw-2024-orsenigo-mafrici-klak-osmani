package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStartLobby;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

/**
 * Represents an interactive component responsible for guiding the user through the process of creating a game lobby.
 * It manages the input for setting the lobby name and the target number of users, validates the inputs,
 * and sends a packet to start the lobby creation process. This component handles state transitions and
 * displays appropriate messages for input validation errors and status updates during the lobby creation process.
 */
public class LobbyCreator extends  InteractiveComponent {
    private int targetNumberUsers;
    private String lobbyName;
    private final ClientModel model;

    private boolean invalidPlayerNumber;

    private ErrorsDictionary nameAlreadyTaken;

    /**
     * Initializes a LobbyCreator instance with the necessary attributes and prepares it to guide the user
     * through the process of creating a game lobby. The LobbyCreator interacts with the client-side model
     * to manage lobby creation requests and handles input validation for setting the lobby name and the
     * target number of users. It observes changes in the lobby creation process and updates its state
     * accordingly.
     */
    public LobbyCreator() {
        super(1);
        this.model = ClientModel.getInstance();
        nameAlreadyTaken = null;
        refreshObserved();
    }


    /**
     * Handles user input for creating a game lobby.
     * This method processes the input to set the lobby name and the target number of users,
     * and sends a packet to start the lobby. It also manages state transitions based on input validation.
     *
     * @param input the user input string to be processed
     * @return the state of the input handling process as an InteractiveComponentReturns enum
     */
    @Override
    public InteractiveComponentReturns handleInput(String input) {
        // Reset the invalid player number flag
        invalidPlayerNumber = false;
        nameAlreadyTaken = null;

        // Process input through superclass method
        InteractiveComponentReturns superReturn = super.handleInput(input);
        if (superReturn == InteractiveComponentReturns.QUIT) {
            return superReturn;
        } else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        int inputCounter = getInputCounter();

        // Handle input for lobby name
        if (inputCounter == 0) {
            lobbyName = input;
            incrementInputCounter();
        }
        // Handle input for the target number of users
        else if (inputCounter == 1) {
            // Validate input for the number of users
            if (InputValidator.checkInputBound(input, 2, 4)) {
                targetNumberUsers = Integer.parseInt(input);
                // Send packet to start the lobby
                model.getClientConnector().sendPacket(new CSPStartLobby(lobbyName, targetNumberUsers));
                incrementInputCounter();
                return InteractiveComponentReturns.COMPLETE;
            } else {
                // Handle invalid number of users
                invalidPlayerNumber = true;
            }
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getKeyword() {
        return "createlobby";
    }

    /**
     * This method returns an empty string as there is no specific description associated with the
     * LobbyCreator component.
     *
     * @return an empty string
     */
    @Override
    public String getDescription() {
        return "";
    }

    /**
     * Prints the current state of the lobby creation process to the console. It handles various scenarios
     * such as displaying errors for invalid player numbers or name conflicts, prompting for lobby name
     * and number of users, and indicating waiting states for server responses.
     * <p>
     * The method also checks for the presence of errors related to lobby name availability and updates
     * the input counter based on the state of nameAlreadyTaken. It invokes the superclass print method
     * to print any inherited messages.
     */
    //TODO move the update of the boolean in the handle input
    @Override
    public void print() {
        super.print();

        if(nameAlreadyTaken == null)
            nameAlreadyTaken = model.getJoinLobbyError();


        if(nameAlreadyTaken != null)
            resetInputCounter();

        if(invalidPlayerNumber){
            System.out.println("The number of users must be between 2 and 4!");
        }
        if(nameAlreadyTaken != null){
            System.out.println("You can't use "+lobbyName+" as a name for the lobby, choose a name that is not already taken.");
        }

        if (getInputCounter() == 0)
            System.out.println("\nEnter lobby name: ");
        else if (getInputCounter() == 1) {
            System.out.println("\nLobby name: "+lobbyName);
            System.out.println("\nEnter the number of wanted users in the game: ");
        }
        else if (getInputCounter() == 2) {
            System.out.println("Waiting for server Response.");
        }
    }

    /**
     * This method overrides the cleanObserved method in InteractiveComponent but does not implement any
     * specific cleaning actions for observed components within the LobbyCreator.
     */
    @Override
    public void cleanObserved() {

    }

    /**
     * This method overrides the refreshObserved method in InteractiveComponent but does not implement any
     * specific actions for refreshing observed components within the LobbyCreator.
     */
    @Override
    public void refreshObserved() {

    }
}
