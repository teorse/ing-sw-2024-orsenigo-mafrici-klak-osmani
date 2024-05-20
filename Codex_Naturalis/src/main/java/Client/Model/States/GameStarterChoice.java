package Client.Model.States;

import Client.Model.ClientModel;
import Client.Model.Records.CardRecord;
import Client.View.TextUI;
import Model.Cards.Corner;
import Model.Cards.CornerOrientation;
import Model.Cards.CornerType;
import Model.Player.PlayerStates;
import Model.Utility.Artifacts;

import Network.ClientServer.Packets.CSPPlayCard;

/**
 * Represents the state where the player makes choices at the start of the game.
 * Upon entering this state, the command-line interface is cleared, and the game title is displayed.
 * Additionally, the method prints the available choices for the player.
 *
 * <p>This state allows the player to select the side (front or back) where they want to place the starter card.
 * It displays details of the card starter, including central artifacts, front corners, and back corners.
 * The player can choose to place the card either on the front side or the back side.
 * </p>
 *
 */
public class GameStarterChoice extends ClientState {

    /**
     * Represents the state where the player makes choices at the start of the game.
     * Upon entering this state, the command-line interface is cleared, and the game title is displayed.
     * Additionally, the method prints the available choices for the player.
     *
     * @param model the client model
     */
    public GameStarterChoice(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

    /**
     * Prints the details of the card starter and prompts the user to choose the side to place the card.
     * <p>
     * This method displays the central artifacts, front corners, and back corners of the card starter.
     * Additionally, it prompts the user to select the side (front or back) where they want to place the card.
     */
    @Override
    public void print() {
        showCardStarter();
        System.out.println("\n" +
                    """
                    On which side do you want to place the card? Enter your choice:
                     1 - Front
                     2 - Back""");
    }

    /**
     * Handles the user input for playing a card.
     * <p>
     * This method checks if the input is a binary choice (1 or 2) indicating whether to play the card face up or face down.
     * If the input is valid, it sends a packet to play the card with the specified orientation (face up or face down).
     *
     * @param input the user input indicating the choice to play the card face up (1) or face down (2)
     */
    @Override
    public void handleInput(String input) {
        if (TextUI.getBinaryChoice(input)) {
            boolean faceUp = (Integer.parseInt(input) == 1);
            model.getClientConnector().sendPacket(new CSPPlayCard(0, 0, faceUp));
        }
    }

    /**
     * Moves the client to the next state based on the success of the previous operation and the player's current state.
     * If the previous operation was successful and the player's state is set to "DRAW", transitions to the GameInitialDrawState.
     * Otherwise, prints an error message and remains in the current state, prompting the user to try again.
     */
    @Override
    public void nextState() {
        if (model.isOperationSuccesful()) {
            if (model.getMyPlayerState() == PlayerStates.DRAW_RESOURCE)
                model.setClientState(new GameInitialDrawState(model));
            else if (model.getMyPlayerState() == PlayerStates.WAIT)
                model.setClientState(new GameWaitState(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }

    /**
     * Displays the details of the starter card, including central artifacts, front corners, and back corners.
     * The central artifacts, front corners, and back corners are retrieved from the player's secret information record.
     */
    private void showCardStarter() {
        CardRecord cardStarter = model.getPlayerSecretInfoRecord().cardsHeld().keySet().stream().toList().getFirst();

        //Prints the central artifacts of the card
        System.out.println("Central Artifacts:");
        for (Artifacts artifacts : cardStarter.centralArtifacts().keySet()) {
            System.out.println(" - " + artifacts.name() + ": " + cardStarter.centralArtifacts().get(artifacts));
        }

        //Prints the front corners of the card
        System.out.println("Front Corners:");
        showCornersDetails(cardStarter,true);

        //Prints the back corners of the card
        System.out.println("Back Corners:");
        showCornersDetails(cardStarter,false);
    }

    /**
     * Displays details of the corners of a card based on the provided card record and the face-up status.
     * This method iterates through the corners of the card and prints details about each corner if it matches the face-up status.
     *
     * @param cardStarter The card record containing information about the card.
     * @param faceUp      A boolean indicating whether the details of the face-up or face-down corners should be displayed.
     */
    private void showCornersDetails(CardRecord cardStarter, boolean faceUp) {
        for (CornerOrientation co : cardStarter.corners().keySet()) {
            Corner corner = cardStarter.corners().get(co);
            if (co.isFaceUp() == faceUp) {
                //Details about the corners
                System.out.print(" - " + co.getCornerDirection().name() + ":");
                if (corner.getCornerType() == CornerType.ARTIFACT)
                    System.out.println(" " + corner.getArtifact());
                else
                    System.out.println(" " + corner.getCornerType());
            }
        }
    }
}