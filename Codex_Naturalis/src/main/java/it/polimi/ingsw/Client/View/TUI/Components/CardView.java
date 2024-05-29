package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerOrientation;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;

import java.io.PrintStream;

public class CardView extends Component{
    private final CardRecord card;

    public CardView(CardRecord card) {
        this.card = card;
    }


    @Override
    public void print() {
        out.println("Artifact Type: " + card.cardColor());

        //Prints the points of the card
        out.println("Points: " + card.points());

        //Prints the corners of the card their details
        out.println("Front Corners:");
        for (CornerOrientation co : card.corners().keySet()) {
            Corner corner = card.corners().get(co);

            //Details about the corners
            out.print(" - " + co.getCornerDirection().name() + ":");
            if (corner.getCornerType() == CornerType.ARTIFACT)
                out.println(" " + corner.getArtifact());
            else
                out.println(" " + corner.getCornerType());
        }
        out.println("Back Corners: all empty");

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
}
