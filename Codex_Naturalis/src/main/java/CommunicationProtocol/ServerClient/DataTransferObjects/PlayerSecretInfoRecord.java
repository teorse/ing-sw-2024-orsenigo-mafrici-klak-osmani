package CommunicationProtocol.ServerClient.DataTransferObjects;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a record containing secret information about a player in a game.
 *
 * <p>A {@code PlayerSecretInfoRecord} contains details such as the cards held by the player and their secret objectives.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record PlayerSecretInfoRecord(List<CardRecord> cardsHeld, Map<CardRecord, Boolean> cardPlayability, ObjectiveRecord secretObjectives)
        implements Serializable {

    /**
     * Constructs a new {@code PlayerSecretInfoRecord} with the specified parameters.
     *
     * @param cardsHeld a list containing cards held by the player in the specific order that is also memorized by the server
     * @param cardPlayability map containing the playability value for each card held by the player
     * @param secretObjectives the secret objectives of the player
     */
    public PlayerSecretInfoRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}