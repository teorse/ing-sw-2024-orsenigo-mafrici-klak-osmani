package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerOrientation;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;

public class CardView extends Component{
    private final CardRecord card;

    public CardView(CardRecord card) {
        this.card = card;
    }


    @Override
    public void print() {
        if (card.centralArtifacts() != null) {
            System.out.println("\nCentral Artifacts:");
            for (Artifacts artifacts : card.centralArtifacts().keySet()) {
                System.out.println(" - " + artifacts.name() + ": " + card.centralArtifacts().get(artifacts));
            }
        }

        if(card.cardColor() != null)
            out.println("Artifact Type: " + card.cardColor());

        //Prints the points of the card
        if(card.cardColor() != null)
            out.println("Points: " + card.points());

        //Prints the corners of the card their details
        //Prints the front corners of the card
        System.out.println("Front Corners:");
        showCornersDetails(card,true);

        //Prints the back corners of the card
        System.out.println("Back Corners:");
        showCornersDetails(card,false);

        //Prints if the card needs to cover corners in order to gain points for the player
        if(card.requiresCorner()) {
            out.println("Requires Corner");
        }

        //Prints the artifact needed by the card
        if(card.requiredArtifact() != Artifacts.NULL && card.requiredArtifact() != null) {
            out.println("Required Object: " + card.requiredArtifact());
        }

        //Prints the existing constraints
        if (card.constraint() != null) {
            out.println("Constraints:");
            for (Artifacts artifacts : card.constraint().keySet()) {
                out.println(" - " + artifacts + ": " + card.constraint().get(artifacts));
            }
        }
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
