package Client.Model.Records;

import Model.Player.CardMap;
import Model.Player.CardVisibility;
import Model.Utility.Artifacts;
import Model.Utility.Coordinates;

import java.util.List;
import java.util.Map;

/**
 * Represents a view of a `CardMap` for visual representation in the user interface.
 * <p>
 * The `CardMapView` class provides a visual representation of a `CardMap`, containing:
 * - A 2D array (`map`) representing the board with `CardView` objects.
 * - A map (`cardsPlaced`) of placed cards with their `Coordinates` and `CardVisibility`.
 * - A list of available placements where new cards can be placed.
 * - A counter for artifacts on the board.
 * - The side length of the board (`boardSide`), which is calculated based on the maximum coordinate in the original `CardMap`.
 * <p>
 * This class is designed to be used in user interfaces to display the current state of a `CardMap`,
 * showing where cards are placed, where new cards can be placed, and artifact counts.
 * <p>
 * It includes a constructor to initialize the `CardMapView` from a given `CardMap`, adjusting coordinates to the new board size.
 * The class also provides getter methods to access key attributes, including:
 * - The board side length.
 * - The map of placed cards.
 * - The list of available placements.
 * - The artifact counter.
 *
 * @see CardMap
 */
public record CardMapRecord(Map<Coordinates, CardVisibility> cardsPlaced, List<Coordinates> availablePlacements,
                            Map<Artifacts, Integer> artifactsCounter, List<Coordinates> coordinatesPlaced) {
    /**
     * Constructs a `CardMapView` with specific attributes.
     * <p>
     * This constructor initializes a `CardMapView` with the given parameters:
     * - The `cardsPlaced` map containing the current card placements.
     * - The `availablePlacements` list indicating where new cards can be placed.
     * - The `artifactsCounter` that tracks the count of various artifacts.
     * - The `boardSide` representing the size of the board.
     * <p>
     * It also adjusts the coordinates of the placed cards and available placements to fit the specified board side,
     * ensuring proper alignment for visualization.
     *
     * @param cardsPlaced         The `Map` of coordinates to `CardVisibility` representing the current card placements.
     * @param availablePlacements The `List` of coordinates indicating available placements for new cards.
     * @param artifactsCounter    The `Map` tracking the count of various artifacts.
     */
    public CardMapRecord {
    }

    public CardRecord getCard(Coordinates c) {
        return this.cardsPlaced.get(c).getCard().toRecord();
    }
}