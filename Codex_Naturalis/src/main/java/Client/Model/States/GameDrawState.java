package Client.Model.States;

import Client.Model.ClientModel;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Game.CardPoolTypes;
import Model.Player.PlayerStates;
import Network.ClientServer.Packets.CSPDrawCard;

public class GameDrawState extends ClientState{
    CardPoolTypes cardPoolTypes;
    int cardChoice;

    public GameDrawState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        print();
    }

    @Override
    public void print() {
        //TODO fix null pool cards
        textUI.zoomCardPool(CardPoolTypes.RESOURCE);
        textUI.zoomCardPool(CardPoolTypes.GOLDEN);
        if (inputCounter == 0)
            System.out.println("""
                    Enter from which pool you want to draw:
                     1 - Resource
                     2 - Golden""");
        else if (inputCounter == 1)
            System.out.println();
    }

    /**
     * Handles user input to draw a card from the specified card pool.
     *
     * <p>This method processes input based on the current step of the input sequence:
     * <ul>
     *   <li>Step 0: Checks if the input corresponds to a binary choice (1 or 2) to select the card pool type (resource or golden).
     *       If valid, advances to the next step and stores the chosen card pool type.</li>
     *   <li>Step 1: Checks if the input corresponds to a valid card choice index within the specified card pool type.
     *       If valid, advances to the next step and stores the chosen card index.</li>
     * </ul>
     * Once both steps are completed, the method sends a packet to the client connector to draw the selected card from the specified card pool.
     *
     * @param input The user input to be processed.
     */
    @Override
    public void handleInput(String input) {
        if (inputCounter == 0) {
            if (UserInterface.getBinaryChoice(input)) {
                if (Integer.parseInt(input) == 1)
                    cardPoolTypes = CardPoolTypes.RESOURCE;
                else
                    cardPoolTypes = CardPoolTypes.GOLDEN;
                inputCounter++;
            }
            print();
        } else if (inputCounter == 1) {
            if (UserInterface.checkInputBound(input,1,3)) {
                cardChoice = Integer.parseInt(input);
                inputCounter++;
            }
            print();
        } else
            model.getClientConnector().sendPacket(new CSPDrawCard(cardPoolTypes,cardChoice));
    }

    @Override
    public void nextState() {
        if (model.isOperationSuccesful()) {
            if (!UserInterface.isLastOnlinePlayer(model)) {
                if (myPR.playerState() == PlayerStates.WAIT)
                    model.setClientState(new GameWaitState(model));
            } else
                model.setClientState(new GameOverState(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}
