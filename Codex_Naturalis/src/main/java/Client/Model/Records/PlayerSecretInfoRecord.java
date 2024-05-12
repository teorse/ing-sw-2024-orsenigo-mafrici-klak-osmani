package Client.Model.Records;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a record containing secret information about a player in a game.
 *
 * <p>A {@code PlayerSecretInfoRecord} contains details such as the cards held by the player and their secret objectives.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record PlayerSecretInfoRecord(Map<CardRecord, Boolean> cardsHeld, ObjectiveRecord secretObjectives)
        implements Serializable {

    /**
     * Constructs a new {@code PlayerSecretInfoRecord} with the specified parameters.
     *
     * @param cardsHeld a map containing cards held by the player and their visibility status
     * @param secretObjectives the secret objectives of the player
     */
    public PlayerSecretInfoRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}