package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStartLobby;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.ErrorsDictionary;

public class LobbyCreator extends  InteractiveComponent {
    private int targetNumberUsers;
    private String lobbyName;
    private final ClientModel model;

    private boolean invalidPlayerNumber;

    private ErrorsDictionary nameAlreadyTaken;


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

    @Override
    public String getKeyword() {
        return "createlobby";
    }

    @Override
    public String getDescription() {
        return "";
    }

    //TODO move the update of the boolean in the handle input
    @Override
    public void print() {
        super.print();

        if(nameAlreadyTaken == null)
            nameAlreadyTaken = model.getJoinLobbyError();

        int inputCounter = getInputCounter();
        if(nameAlreadyTaken != null)
            inputCounter = 0;


        if (inputCounter == 0)
            System.out.println("\nEnter lobby name: ");
        else if (inputCounter == 1) {
            System.out.println("\nLobby name: "+lobbyName);
            System.out.println("\nEnter the number of wanted users in the game: ");
        }
        else if (inputCounter == 2) {
            System.out.println("Waiting for server Response.");
        }

        if(invalidPlayerNumber){
            System.out.println("The number of users must be between 2 and 4!");
        }
        if(nameAlreadyTaken != null){
            System.out.println("You can't use "+lobbyName+" as a name for the lobby, choose a name that is not already taken.");
        }
    }

    @Override
    public void cleanObserved() {

    }

    @Override
    public void refreshObserved() {

    }
}
