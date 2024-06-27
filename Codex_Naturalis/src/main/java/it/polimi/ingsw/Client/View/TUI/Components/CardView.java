package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerOrientation;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;

/**
 * The CardView class represents the view of a card in the game.
 * It displays various details about the card such as artifacts, points, corners, and constraints.
 */
public class CardView extends Component {
    private final CardRecord card;

    /**
     * Constructs a new CardView with the specified card.
     *
     * @param card the card to be displayed
     */
    public CardView(CardRecord card) {
        this.card = card;
    }

    /**
     * Prints the details of the card.
     * It includes the central artifacts, artifact type, points, corners, required artifacts, and constraints.
     */
    @Override
    public void print() {
        if (!ClientModel.getInstance().getFancyGraphics()) {
            //If the game is not set to have fancy graphics then print the old way

            // Prints the central artifacts of the card
            if (card.centralArtifacts() != null) {
                System.out.println("\nCentral Artifacts:");
                for (Artifacts artifacts : card.centralArtifacts().keySet()) {
                    System.out.println(" - " + artifacts.name() + ": " + card.centralArtifacts().get(artifacts));
                }
            }

            // Prints the artifact type of the card
            if (card.cardColor() != null)
                out.println("Artifact Type: " + card.cardColor());

            // Prints the points of the card
            if (card.cardColor() != null)
                out.println("Points: " + card.points());

            // Prints the front corners of the card
            System.out.println("Front Corners:");
            showCornersDetails(card, true);

            // Prints the back corners of the card
            System.out.println("Back Corners:");
            showCornersDetails(card, false);

            // Prints if the card requires corners to gain points
            if (card.requiresCorner()) {
                out.println("Requires Corner");
            }

            // Prints the required artifact of the card
            if (card.requiredArtifact() != Artifacts.NULL && card.requiredArtifact() != null) {
                out.println("Required Object: " + card.requiredArtifact());
            }

            // Prints the existing constraints of the card
            if (card.constraint() != null) {
                out.println("Constraints:");
                for (Artifacts artifacts : card.constraint().keySet()) {
                    out.println(" - " + artifacts + ": " + card.constraint().get(artifacts));
                }
            }
        }
        else{
            new CardViewPretty(card, true, true).print();
        }
    }

    /**
     * Displays the details of the card's corners.
     *
     * @param cardStarter the card whose corners are to be displayed
     * @param faceUp      true if the front corners are to be displayed, false for back corners
     */
    private void showCornersDetails(CardRecord cardStarter, boolean faceUp) {
        for (CornerOrientation co : cardStarter.corners().keySet()) {
            Corner corner = cardStarter.corners().get(co);
            if (co.isFaceUp() == faceUp) {
                // Details about the corners
                System.out.print(" - " + co.getCornerDirection().name() + ":");
                if (corner.getCornerType() == CornerType.ARTIFACT)
                    System.out.println(" " + corner.getArtifact());
                else
                    System.out.println(" " + corner.getCornerType());
            }
        }
    }
}
