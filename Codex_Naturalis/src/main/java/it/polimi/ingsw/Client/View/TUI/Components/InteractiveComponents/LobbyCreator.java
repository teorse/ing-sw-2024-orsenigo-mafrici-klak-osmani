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

    private boolean invalidLobbyName;
    private boolean invalidPlayerNumber;

    private ErrorsDictionary nameAlreadyTaken;


    public LobbyCreator() {
        super(1);
        this.model = ClientModel.getInstance();
        nameAlreadyTaken = null;
        refreshObserved();
    }


    @Override
    public InteractiveComponentReturns handleInput(String input) {
        invalidLobbyName = false;
        invalidPlayerNumber = false;

        InteractiveComponentReturns superReturn = super.handleInput(input);
        if(superReturn == InteractiveComponentReturns.QUIT)
            return superReturn;
        else if (superReturn == InteractiveComponentReturns.COMPLETE) {
            return InteractiveComponentReturns.INCOMPLETE;
        }

        int inputCounter = getInputCounter();
        if (inputCounter == 0) {
            if (InputValidator.isNameValid(input)) {
                lobbyName = input;
                incrementInputCounter();
            } else
                invalidLobbyName = true;

        }
        else if (inputCounter == 1) {
            if (InputValidator.checkInputBound(input, 2, 4)) {
                targetNumberUsers = Integer.parseInt(input);
                model.getClientConnector().sendPacket(new CSPStartLobby(lobbyName, targetNumberUsers));
                incrementInputCounter();
                return InteractiveComponentReturns.COMPLETE;
            } else {
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

        if(invalidLobbyName){
            System.out.println("Invalid name. The name must have length between 4 and 14 characters, and start with a letter!.");
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
