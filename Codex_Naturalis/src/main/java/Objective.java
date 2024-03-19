import java.util.Map;

public abstract class Objective {
    /**
     * String containing the textual description of the objective.
     */
    private String description;
    /**
     * Integer containing the amount of points held by the objective.<br>
     * For each separate instance of completion of the objective, the same player can be given the points multiple times.
     */
    private int points;

    /**
     * Default Constructor.<br>
     * Defines objective description and number of points held.
     * @param description   String description of the Objective.
     * @param points        Int number of points held by the objective.
     */
    public Objective(String description, int points){
        this.description = description;
        this.points = points;
    }

    /**
     * Method to get the text description of the objective.
     * @return  String containing objective description.
     */
    public String getDescription() {
        return description;
    }
}
