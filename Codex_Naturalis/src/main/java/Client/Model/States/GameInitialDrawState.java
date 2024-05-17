package Client.Model.States;

import Client.Model.ClientModel;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Game.CardPoolTypes;
import Model.Player.PlayerStates;
import Network.ClientServer.Packets.CSPDrawCard;

public class GameInitialDrawState extends ClientState{
    public GameInitialDrawState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

    @Override
    public void print() {
        if (inputCounter < 2)
            textUI.zoomCardPool(CardPoolTypes.RESOURCE);
        else
            textUI.zoomCardPool(CardPoolTypes.GOLDEN);
        System.out.println("\nEnter a number between 1 and 3 to pick a card: ");
    }

    @Override
    public void handleInput(String input) {
        if (UserInterface.checkInputBound(input,1,3)) {
            int choice = Integer.parseInt(input);
            if (inputCounter < 2) {
                model.getClientConnector().sendPacket(new CSPDrawCard(CardPoolTypes.RESOURCE, choice - 2));
            }
            else {
                model.getClientConnector().sendPacket(new CSPDrawCard(CardPoolTypes.GOLDEN, choice - 2));
            }
        } else
            print();
    }

    @Override
    public void nextState() {
        if (model.isOperationSuccesful()) {
            if (model.getMyPlayerState() == PlayerStates.DRAW) {
                inputCounter++;
                print();
            } else if (model.getMyPlayerState() == PlayerStates.PICK_OBJECTIVE) {
                model.setClientState(new GamePickObjectiveState(model));
            }
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}
