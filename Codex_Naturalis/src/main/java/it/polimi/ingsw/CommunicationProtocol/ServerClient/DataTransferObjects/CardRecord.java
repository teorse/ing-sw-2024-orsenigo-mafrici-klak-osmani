package it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects;

import it.polimi.ingsw.Server.Model.Game.Cards.Corner;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerOrientation;
import it.polimi.ingsw.Server.Model.Game.Artifacts;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a record containing information about a card in a game.
 *
 * <p>A {@code CardRecord} consists of the color of the card, points associated with the card, corners of the card,
 * a flag indicating if the card requires a corner, required artifact for the card, constraints, and central artifacts.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record CardRecord(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners,
                         boolean requiresCorner, Artifacts requiredArtifact, Map<Artifacts, Integer> constraint,
                         Map<Artifacts, Integer> centralArtifacts) implements Serializable {

    /**
     * Constructs a new {@code CardRecord} with the specified parameters.
     *
     * @param cardColor the color of the card
     * @param points the points associated with the card
     * @param corners the corners of the card
     * @param requiresCorner indicates if the card requires a corner
     * @param requiredArtifact the required artifact for the card
     * @param constraint the constraints associated with the card
     * @param centralArtifacts the central artifacts associated with the card
     */
    public CardRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}