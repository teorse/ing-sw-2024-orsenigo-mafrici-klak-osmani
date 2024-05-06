package Client.Model.Records;

import Model.Utility.Artifacts;

import java.util.List;

/**
 * Represents a view of the game table, encapsulating the visual representation of card pools and shared objectives.
 * <p>
 * The TableView class provides an overview of the game's table, including:
 * - The top deck cards for resource and golden card pools.
 * - The lists of visible cards for resource and golden pools.
 * - The list of shared objectives.
 * <p>
 * It is constructed based on a given Table instance and can be used to display information
 * about the game's card pools and shared objectives in a human-readable format.
 * <p>
 * The attributes and methods in this class are designed to be used in a view or display layer
 * to present game-related information to users.
 *
 * @param topDeckResource ATTRIBUTES
 */
public record TableRecord(Artifacts topDeckResource, Artifacts topDeckGolden, List<CardRecord> visibleCardRecordResource,
                          List<CardRecord> visibleCardRecordGolden, List<ObjectiveRecord> sharedObjectives) {

    //CONSTRUCTOR
    /**
     * Constructs a `TableView` object with the specified card pools and shared objectives.
     * <p>
     * This constructor initializes:
     * - The list of visible card views for resource and golden card pools.
     * - The top deck cards for the resource and golden card pools.
     * - The list of shared objectives.
     * <p>
     * It iterates over the given card pools to populate the resource and golden card views.
     * For each card pool, it sets the top deck card and creates a list of visible card views.
     * It also populates the shared objectives from the given list of objectives.
     *
     * @param sharedObjectives A list of `Objective` objects representing the shared objectives in the game.
     */
    public TableRecord {
    }
}