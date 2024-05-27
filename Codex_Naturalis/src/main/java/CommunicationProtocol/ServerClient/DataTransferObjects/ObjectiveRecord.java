package CommunicationProtocol.ServerClient.DataTransferObjects;

import java.io.Serializable;

/**
 * Represents a record containing information about an objective in a game.
 *
 * <p>An {@code ObjectiveRecord} contains details such as the description and the points associated with the objective.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record ObjectiveRecord(String description, int points) implements Serializable {

    /**
     * Constructs a new {@code ObjectiveRecord} with the specified parameters.
     *
     * @param description the description of the objective
     * @param points the points associated with the objective
     */
    public ObjectiveRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}
