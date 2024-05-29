package it.polimi.ingsw.Client.View.TUI.Components;

import it.polimi.ingsw.Client.View.TUI.TerminalColor;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardVisibilityRecord;
import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;

import java.io.PrintStream;

public class PlacedCardView extends Component{
    private final CardVisibilityRecord card;
    private final PrintStream out;

    public PlacedCardView(CardVisibilityRecord card) {
        this.card = card;
        out = new PrintStream(System.out, true);
    }


    @Override
    public void print() {
        out.println("\nArtifact Type: " + card.cardColor());

        //Prints the corners of the card their details
        out.println("Corners: (Those covered will be printed in red)");
        for (CornerDirection cd : card.corners().keySet()) {
            // Retrieve the corner based on its orientation
            Corner corner = card.corners().get(cd);

            // Check if the corner is covered, indicated by the color red
            if (!card.cornerVisibility().get(cd)) {
                out.print(TerminalColor.RED);
            }

            // Display the corner details
            out.print(" - " + cd.name() + ":");

            // Print the artifact or corner type based on the corner's contents
            if (corner.getCornerType() == CornerType.ARTIFACT) {
                out.println(" " + corner.getArtifact());
            } else {
                out.println(" " + corner.getCornerType());
            }

            // Reset the terminal color to avoid affecting other output
            out.print(TerminalColor.RESET);
        }
    }
}
