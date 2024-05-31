package it.polimi.ingsw.Client.View.TUI.Components.InteractiveComponents;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.Model.Lobby;
import it.polimi.ingsw.Client.View.TUI.TextUI;
import it.polimi.ingsw.CommunicationProtocol.ClientServer.Packets.CSPChangeColor;
import it.polimi.ingsw.Server.Model.Lobby.LobbyUserColors;

import java.util.List;

public class ColorPicker extends InteractiveComponent{
    //ATTRIBUTES
    private final ClientModel model;
    private final List<LobbyUserColors> availableUserColors;





    //CONSTUCTOR
    public ColorPicker() {
        this.model = ClientModel.getInstance();
        this.availableUserColors = Lobby.getInstance().getAvailableUserColors();
    }





    //METHODS
    @Override
    public InteractiveComponentReturns handleInput(String input) {

        if(input.equalsIgnoreCase("BACK"))
            return super.handleInput(input);

        if (TextUI.checkInputBound(input, 1, availableUserColors.size())) {
            model.getClientConnector().sendPacket(new CSPChangeColor(model.getLobbyRecord().availableUserColors().get(Integer.parseInt(input) - 1)));
            return InteractiveComponentReturns.COMPLETE;
        }
        else {
            //TODO system out 1 in ColorPicker
            System.out.println("Invalid input. Type a number between 1 and " + availableUserColors.size());
            return InteractiveComponentReturns.INCOMPLETE;
        }
    }

    @Override
    public String getKeyword() {
        return "color";
    }

    @Override
    public void print() {
        if (!availableUserColors.isEmpty()) {
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
}
