package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.Model.ClientModel;
import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardVisibilityRecord;
import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;

/**
 * The PlacedCardView class represents a component responsible for displaying
 * the details of a card that has been placed in the game, including its artifact
 * type and the visibility status of its corners in the TUI.
 */
public class PlacedCardView extends Component {
    private final CardVisibilityRecord card;

    /**
     * Constructs a new PlacedCardView instance with the given CardVisibilityRecord.
     * @param card The card visibility record to display.
     */
    public PlacedCardView(CardVisibilityRecord card) {
        this.card = card;
    }

    /**
     * Prints the details of the placed card to the terminal.
     * Displays the artifact type and the details of each corner, marking covered
     * corners in red if they are not visible.
     */
    @Override
    public void print() {
        if (!ClientModel.getInstance().getFancyGraphics()) {
            out.println("\nArtifact Type: " + card.cardColor());

            // Print the corners of the card and their details
            out.println("Corners: (Covered corners will be printed in red)");
            for (CornerDirection cd : card.corners().keySet()) {
                // Retrieve the corner based on its direction
                Corner corner = card.corners().get(cd);

                // Check if the corner is covered, indicated by red color
                if (!card.cornerVisibility().get(cd)) {
                    out.print(TerminalColor.RED);
                }

                // Display the corner direction and its contents
                out.print(" - " + cd.name() + ":");

                // Print the artifact or corner type based on the corner's content
                if (corner.getCornerType() == CornerType.ARTIFACT) {
                    out.println(" " + corner.getArtifact());
                } else {
                    out.println(" " + corner.getCornerType());
                }

                // Reset the terminal color to avoid affecting other output
                out.print(TerminalColor.RESET);
            }
        }
        else{
            new PlacedCardViewPretty(card).print();
        }
    }
}
