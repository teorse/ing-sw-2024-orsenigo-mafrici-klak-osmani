package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPStartLobby;

public class LobbyCreator extends  InteractiveComponent {
    private int targetNumberUsers;
    private String lobbyName;
    private final ClientModel model;


    public LobbyCreator() {
        this.model = ClientModel.getInstance();
    }


    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (inputCounter == 0) {
            if (TextUI.isNameValid(input)) {
                lobbyName = input;
                inputCounter++;
            } else
                System.out.println("Invalid name. The name must have lenght between 4 and 14 characters, and start with a letter!.");
        }else if (inputCounter == 1) {
            if (TextUI.checkInputBound(input, 2, 4)) {
                targetNumberUsers = Integer.parseInt(input);
                model.getClientConnector().sendPacket(new CSPStartLobby(lobbyName, targetNumberUsers));
                return true;
            } else {
                System.out.println("The number of users must be between 2 and 4!");
            }
        }
        return false;
    }

    @Override
    public String getKeyword() {
        return "";
    }

    @Override
    public void print() {
        if (inputCounter == 0)
            System.out.println("\nEnter lobby name: ");
        else if (inputCounter == 1) {
            System.out.println("\nEnter the number of wanted users in the game: ");
        }
    }
}
