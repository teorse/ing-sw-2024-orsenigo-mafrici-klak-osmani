package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.ViewStates.ViewState;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStartLobby;

public class LobbyCreator extends  InteractiveComponent {
    private int targetNumberUsers;
    private String lobbyName;
    private final ClientModel model;

    private boolean invalidLobbyName;
    private boolean invalidPlayerNumber;


    public LobbyCreator(ViewState view) {
        super(view);
        this.model = ClientModel.getInstance();
    }


    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (inputCounter == 0) {
            if (InputValidator.isNameValid(input)) {
                lobbyName = input;
                inputCounter++;
            } else
                invalidLobbyName = true;

        }
        else if (inputCounter == 1) {
            if (InputValidator.checkInputBound(input, 2, 4)) {
                targetNumberUsers = Integer.parseInt(input);
                model.getClientConnector().sendPacket(new CSPStartLobby(lobbyName, targetNumberUsers));
                return InteractiveComponentReturns.COMPLETE;
            } else {
                invalidPlayerNumber = true;
            }
        }
        return InteractiveComponentReturns.INCOMPLETE;
    }

    @Override
    public String getKeyword() {
        return "CreateLobby";
    }

    //TODO move the update of the boolean in the handle input
    @Override
    public void print() {
        if (inputCounter == 0)
            System.out.println("\nEnter lobby name: ");
        else if (inputCounter == 1) {
            System.out.println("\nEnter the number of wanted users in the game: ");
        }

        if(invalidLobbyName){
            invalidLobbyName = false;
            System.out.println("Invalid name. The name must have length between 4 and 14 characters, and start with a letter!.");
        }
        if(invalidPlayerNumber){
            invalidPlayerNumber = false;
            System.out.println("The number of users must be between 2 and 4!");
        }

    }

    @Override
    public void cleanObserved() {

    }
}
