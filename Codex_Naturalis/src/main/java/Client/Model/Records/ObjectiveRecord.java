package Client.Model.Records;

/**
 * Represents a view of an objective in a game, providing information about its description, points, and requirements.
 * <p>
 * The `ObjectiveView` class is designed to represent the visual aspects of an `Objective`.
 * It can hold details such as:
 * - A textual description of the objective.
 * - The number of points the objective is worth.
 * - A geometric pattern, if applicable.
 * - Numeric requirements, if applicable.
 * <p>
 * The constructor determines whether the `Objective` is geometric or numeric, and initializes the appropriate
 * attributes accordingly.
 * <p>
 * This class is useful for displaying objective information in a user interface or other output formats.
 *
 * @param description ATTRIBUTES
 */
public record ObjectiveRecord(String description, int points) {
    /**
     * Constructs an `ObjectiveView` object with a specified description, points, geometric pattern, and numeric requirements.
     * <p>
     * This constructor initializes:
     * - The description of the objective.
     * - The points awarded by the objective.
     * - The geometric pattern, if applicable.
     * - The numeric requirements, if applicable.
     * The geometric pattern and numeric requirements are mutually exclusive; only one can be non-null at a time.
     *
     * @param description The text description of the objective.
     * @param points      The number of points awarded by the objective.
     */
    public ObjectiveRecord {}
}
