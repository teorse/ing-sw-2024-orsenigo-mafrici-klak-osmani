package CommunicationProtocol.ServerClient.DataTransferObjects;

import Server.Model.Game.Artifacts;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a record containing information about the table in a game.
 *
 * <p>A {@code TableRecord} contains details such as the top deck resource and golden cards, visible card records for resource and golden decks, and shared objectives.
 *
 * <p>This record implements the {@code Serializable} interface to support serialization.
 */
public record TableRecord(Artifacts topDeckResource, Artifacts topDeckGolden,
                          List<CardRecord> visibleCardRecordResource, List<CardRecord> visibleCardRecordGolden,
                          List<ObjectiveRecord> sharedObjectives) implements Serializable {

    /**
     * Constructs a new {@code TableRecord} with the specified parameters.
     *
     * @param topDeckResource the top deck resource
     * @param topDeckGolden the top deck golden card
     * @param visibleCardRecordResource the list of visible card records for the resource deck
     * @param visibleCardRecordGolden the list of visible card records for the golden deck
     * @param sharedObjectives the list of shared objectives
     */
    public TableRecord {
        // No additional implementation needed as records automatically generate a constructor
    }
}