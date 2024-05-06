package Client.Model;

import Model.Utility.Artifacts;
import Model.Utility.Coordinates;

import java.util.Map;

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
 */
public class ObjectiveView {
    //ATTRIBUTES
    private final String description;
    private final int points;
    private Map<Coordinates, Artifacts> geometricPattern;
    private Map<Artifacts, Integer> numericRequirements;

    /**
     * Constructs an `ObjectiveView` object with a specified description, points, geometric pattern, and numeric requirements.
     * <p>
     * This constructor initializes:
     * - The description of the objective.
     * - The points awarded by the objective.
     * - The geometric pattern, if applicable.
     * - The numeric requirements, if applicable.
     *
     * The geometric pattern and numeric requirements are mutually exclusive; only one can be non-null at a time.
     *
     * @param description        The text description of the objective.
     * @param points             The number of points awarded by the objective.
     * @param geometricPattern   A map of coordinates to artifacts, representing a geometric pattern, or null if not applicable.
     * @param numericRequirements A map of artifacts to required quantities, representing numeric requirements, or null if not applicable.
     */
    public ObjectiveView(String description, int points, Map<Coordinates, Artifacts> geometricPattern, Map<Artifacts, Integer> numericRequirements) {
        this.description = description;
        this.points = points;
        this.geometricPattern = geometricPattern;
        this.numericRequirements = numericRequirements;
    }


    //GETTERS
    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    public Map<Coordinates, Artifacts> getGeometricPattern() {
        return geometricPattern;
    }

    public Map<Artifacts, Integer> getNumericRequirements() {
        return numericRequirements;
    }


    //SETTER
    public void setGeometricPattern(Map<Coordinates, Artifacts> geometricPattern) {
        this.geometricPattern = geometricPattern;
    }

    public void setNumericRequirements(Map<Artifacts, Integer> numericRequirements) {
        this.numericRequirements = numericRequirements;
    }
}