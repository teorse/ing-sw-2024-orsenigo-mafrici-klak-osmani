import java.util.Map;

public class ObjectiveGeometric extends Objective{
    /**
     * Map containing the required pattern to satisfy the objective.<br>
     * The Keys of the map are the coordinates of the pattern and the Values are the Resource/Color of the pattern.
     */
    private Map<Coordinates, Resource> cardPattern;

    /**
     * Default Constructor.<br>
     * Defines objective description, number of points held and the required Geometric pattern.
     * @param description   String description of the Objective.
     * @param points        Int number of points held by the objective.
     * @param cardPattern   Map with key Coordinates of the pattern and value resource/color of the pattern.
     */
    public ObjectiveGeometric(String description, int points, Map<Coordinates, Resource> cardPattern){
        super(description, points);
        this.cardPattern = cardPattern;
    }

    /**
     * Method to access the required card Pattern.
     * @return  Map<Coordinates, Resource> map with coordinates and type of resource for the card pattern.
     */
    public Map<Coordinates, Resource> getCardPattern() {
        return cardPattern;
    }
}
