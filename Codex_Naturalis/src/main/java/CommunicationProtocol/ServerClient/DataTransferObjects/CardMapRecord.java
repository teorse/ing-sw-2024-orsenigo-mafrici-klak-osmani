package CommunicationProtocol.ServerClient.DataTransferObjects;

import Server.Model.Game.Utility.Artifacts;
import Server.Model.Game.Utility.Coordinates;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a record containing information about a card map in a game.
 *
 * <p>A {@code CardMapRecord} consists of the cards placed on the map, available placements for cards,
 * artifacts and their counts, and the coordinates of the placed cards.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record CardMapRecord(Map<Coordinates, CardVisibilityRecord> cardsPlaced, List<Coordinates> availablePlacements,
                            Map<Artifacts, Integer> artifactsCounter, List<Coordinates> coordinatesPlaced)
        implements Serializable {

    /**
     * Constructs a new {@code CardMapRecord} with the specified parameters.
     *
     * @param cardsPlaced the cards placed on the map
     * @param availablePlacements the available placements for cards
     * @param artifactsCounter the artifacts and their counts
     * @param coordinatesPlaced the coordinates of the placed cards
     */
    public CardMapRecord {
        // No additional implementation needed as records automatically generate a constructor
    }

    public CardVisibilityRecord getCardVisibilityRecord(Coordinates c) {
        return this.cardsPlaced.get(c);
    }
}