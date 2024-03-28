package Model.Objectives;

import Model.Utility.Artifacts;

import java.util.Map;

public class ObjectiveResource extends Objective{
    /**
     * Map containing the required resources to satisfy the objective.<br>
     * The Keys of the map are the resources and the Values are the number of units required.
     */
    private Map<Artifacts, Integer> requiredResources;

    /**
     * Default Constructor.<br>
     * Defines objective description, number of points held and required Resources.
     * @param description       String description of the Model.Objectives.Objective.
     * @param points            Int number of points held by the objective.
     * @param requiredResources Map with key required resources and value the number of units required.
     */
    public ObjectiveResource(String description, int points, Map<Artifacts, Integer> requiredResources) {
        super(description, points);
        this.requiredResources = requiredResources;
    }

    /**
     * Method to access the required Resources.
     * @return  Map<Resource, Integer> map with Resource and number of units required.
     */
    public Map<Artifacts, Integer> getRequiredResources() {
        return requiredResources;
    }
}
