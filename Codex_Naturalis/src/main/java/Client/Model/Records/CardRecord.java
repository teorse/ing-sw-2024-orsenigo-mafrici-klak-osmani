package Client.Model.Records;

import Model.Cards.Card;
import Model.Cards.Corner;
import Model.Cards.CornerOrientation;
import Model.Utility.Artifacts;

import java.util.Map;

/**
 * Represents a visual representation of a card, encapsulating its attributes for display in the user interface.
 * <p>
 * The `CardView` class provides a simplified view of a card's key details, including:
 * - The card's color.
 * - The points awarded by the card.
 * - The corners and their types (artifact or empty).
 * - The flag indicating if the card requires a specific corner for scoring.
 * - The artifact required by the card, if any.
 * - Any constraints associated with the card, such as specific artifacts needed for play.
 * - The central artifacts if the card is of type `CardStarter`.
 * <p>
 * This class can represent different types of cards, adjusting attributes based on the card's type.
 * For example:
 * - If the card is of type `CardGolden`, it sets requirements related to corner covering and specific artifacts.
 * - If the card is of type `CardStarter`, it includes the central artifacts on the card.
 * <p>
 * The constructor initializes the `CardView` from a given `Card`, allowing the user interface to display information
 * about the card in a simplified and consistent manner.
 * <p>
 * Getter methods are provided to access the various attributes of the card.
 *
 * @see Card
 */

public record CardRecord(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, boolean requiresCorner,
                         Artifacts requiredArtifact, Map<Artifacts, Integer> constraint,
                         Map<Artifacts, Integer> centralArtifacts) {
    /**
     * Constructs a CardView object that represents the visual attributes of a card.
     * <p>
     * This constructor initializes the following attributes based on the type of the given card:
     * - The card's corners and their details.
     * - The card's points.
     * - The card's color.
     * <p>
     * Additionally, if the card is of type `CardGolden`, it sets the required corner flag, required artifact, and constraint.
     * If the card is of type `CardStarter`, it initializes the central artifacts.
     */
    public CardRecord {
    }
}