package Client.Model;

import Model.Player.CardMap;
import Model.Player.CardVisibility;
import Model.Utility.Artifacts;
import Model.Utility.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
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
public class CardMapView {
//    private CardView[][] map;
    private Map<Coordinates, CardVisibility> cardsPlaced; // Here the coordinates are from 0 to boardSide, shifted of sideLength
    private List<Coordinates> availablePlacements;
    private Map<Artifacts, Integer> artifactsCounter;
    private int boardSide;

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
     * @param cardsPlaced The `Map` of coordinates to `CardVisibility` representing the current card placements.
     * @param availablePlacements The `List` of coordinates indicating available placements for new cards.
     * @param artifactsCounter The `Map` tracking the count of various artifacts.
     * @param boardSide The size of the board, used to adjust the map and coordinates accordingly.
     */
    public CardMapView(Map<Coordinates, CardVisibility> cardsPlaced, List<Coordinates> availablePlacements, Map<Artifacts, Integer> artifactsCounter, int boardSide) {
        // Calculate half the board side to adjust coordinates
        int sideLength = (boardSide - 1) / 2;

        // Initialize HashMap for placed cards and ArrayList for available placements
        this.cardsPlaced = new HashMap<>();
        this.availablePlacements = new ArrayList<>();

        // Adjust the coordinates of placed cards to the new board size and populate the map and cardsPlaced
        for (Coordinates co : cardsPlaced.keySet()) {
            Coordinates adjustedCoordinates = new Coordinates(co.getCoordX() + sideLength, co.getCoordY() + sideLength);
            this.cardsPlaced.put(adjustedCoordinates, cardsPlaced.get(co));
        }

        // Adjust the coordinates for the available placements to the new board size
        for (Coordinates co : availablePlacements) {
            this.availablePlacements.add(new Coordinates(co.getCoordX() + sideLength, co.getCoordY() + sideLength));
        }

        this.artifactsCounter = artifactsCounter;
        this.boardSide = boardSide;
    }

    //GETTERS
    public Map<Coordinates, CardVisibility> getCardsPlaced() {
        return cardsPlaced;
    }

    public List<Coordinates> getAvailablePlacements() {
        return availablePlacements;
    }

    public Map<Artifacts, Integer> getArtifactsCounter() {
        return artifactsCounter;
    }

    public int getBoardSide() {
        return boardSide;
    }

    public CardView getCard(Coordinates c) {
        return this.cardsPlaced.get(c).getCard().toCardView();
    }
}