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


public class GameStarterChoice extends ClientState {
    public GameStarterChoice(ClientModel model) {
        super(model);
        TextUI.clearCMD();
        TextUI.displayGameTitle();
        print();
    }

    @Override
    public void print() {
        showCardStarter();
        System.out.println("\n" +
                    """
                    On which side do you want to place the card? Enter your choice:
                     1 - Front
                     2 - Back""");
    }

    @Override
    public void handleInput(String input) {
        if (TextUI.getBinaryChoice(input)) {
            boolean faceUp = (Integer.parseInt(input) == 1);
            model.getClientConnector().sendPacket(new CSPPlayCard(0, 0, faceUp));
        }
    }

    @Override
    public void nextState() {
        if (model.isOperationSuccesful() && model.getMyPlayerState() == PlayerStates.DRAW) {
            model.setClientState(new GameInitialDrawState(model));
        } else {
            System.out.println("The operation failed! Please try again.\n");
            print();
        }
    }

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
