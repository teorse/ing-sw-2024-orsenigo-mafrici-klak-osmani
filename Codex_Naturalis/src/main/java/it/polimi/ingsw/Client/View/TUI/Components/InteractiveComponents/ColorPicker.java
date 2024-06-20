package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Lobby;
import it.polimi.ingsw.Client.Model.RefreshManager;
import it.polimi.ingsw.Client.View.InputValidator;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPChangeColor;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.List;

public class ColorPicker extends InteractiveComponent{
    //ATTRIBUTES
    private final ClientModel model;
    private List<LobbyUserColors> availableUserColors;

    private boolean invalidChoice;





    //CONSTRUCTOR
    public ColorPicker(){
        super();
        this.model = ClientModel.getInstance();

        refreshObserved();

        invalidChoice = false;
    }





    //METHODS
    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (InputValidator.checkInputBound(input, 1, availableUserColors.size())) {
            model.getClientConnector().sendPacket(new CSPChangeColor(Lobby.getInstance().getAvailableUserColors().get(Integer.parseInt(input) - 1)));
            return InteractiveComponentReturns.COMPLETE;
        }
        else {
            invalidChoice = true;
            return InteractiveComponentReturns.INCOMPLETE;
        }
    }

    @Override
    public String getKeyword() {
        return "color";
    }

    @Override
    public void print() {
        availableUserColors = Lobby.getInstance().getAvailableUserColors();

        if(invalidChoice){
            invalidChoice = false;
            System.out.println("Invalid input. Type a number between 1 and " + availableUserColors.size());
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

    @Override
    public void cleanObserved() {
        RefreshManager.getInstance().removeObserved(this, Lobby.getInstance());
    }

    @Override
    public void refreshObserved() {
        RefreshManager.getInstance().addObserved(this, Lobby.getInstance());
    }
}
