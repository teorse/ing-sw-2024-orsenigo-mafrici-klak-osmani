package Client.Model.States;

import Client.Model.ClientModel;
import Client.View.TextUI;
import Client.View.UserInterface;
import Model.Player.PlayerStates;
import Model.Utility.Coordinates;

public class GamePlaceState extends ClientState{
    int cardIndex;
    int coordinateIndex;
    boolean faceUp;

    public GamePlaceState(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        System.out.println("It's your turn!");
        print();
    }

    @Override
    public void print() {
        textUI.showGameBoard();
        textUI.zoomCardsHeld();
        if (inputCounter == 0) {
            System.out.println("Choose a card from your hand to place.");
        } else if (inputCounter == 1) {
            System.out.println("Choose a coordinate to place the card [ROW] [COLUMN]. (On the map are the squares in white with an X)");
        } else if (inputCounter == 2) {
            System.out.println("""
                    On which side do you want to place the card? Enter your choice:
                     1 - Front
                     2 - Back""");
        }
    }

    /**
     * Handles user input based on the current step of the input sequence and the available choices.
     *
     * <p>This method processes input according to the following steps:
     * <ul>
     *   <li>Step 0: Checks if the input corresponds to a valid card index within the player's held cards. If valid, advances to the next step.</li>
     *   <li>Step 1: Obtains coordinates chosen by the user. If valid and available for placement, advances to the next step.</li>
     *   <li>Step 2: Parses the input to determine if the user wants to place the card face up or face down. Advances to the next step upon valid input.</li>
     * </ul>
     * After each step, the method invokes the {@code print} method to display relevant information or prompt the user for further input.
     *
     * @param input The user input to be processed.
     */
    @Override
    public void handleInput(String input) {
        if (inputCounter == 0) {
            if (TextUI.checkInputBound(input,1,3)) {
                cardIndex = Integer.parseInt(input);
                inputCounter++;
            }
        } else if (inputCounter == 1) {
            Coordinates coordinatesChosen = textUI.getInputCoordinates(input);
            if (coordinatesChosen != null && textUI.isAvaiableCoordinates(coordinatesChosen)) {
                coordinateIndex = model.getPlayerCardMapRecord().get(myPR).availablePlacements().indexOf(coordinatesChosen);
                inputCounter++;
            }
        } else if (inputCounter == 2) {
            if (TextUI.getBinaryChoice(input)) {
                faceUp = (Integer.parseInt(input) == 1);
                inputCounter++;
            }
        }
        print();
    }

    @Override
    public void nextState() {
        if (model.isOperationSuccesful()) {
            if (!UserInterface.isLastOnlinePlayer(model)) {
                myPR = UserInterface.myPlayerRecord(model);
                if (myPR.playerState() == PlayerStates.DRAW) {
                    model.setClientState(new GameDrawState(model));
                }
            } else
                model.setClientState(new GameOverState(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }
}

