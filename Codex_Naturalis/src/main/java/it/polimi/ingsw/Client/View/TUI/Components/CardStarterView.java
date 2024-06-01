package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.Observer;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerOrientation;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;

public class CardStarterView extends Component implements Observer {
    private final CardRecord cardStarter;

    public CardStarterView(CardRecord cardStarter) {
        this.cardStarter = cardStarter;
    }

    @Override
    public void update() {
        print();
    }

    @Override
    public void print() {
        //Prints the central artifacts of the card
        System.out.println("\nCentral Artifacts:");
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

    @Override
    public void cleanUp() {

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
