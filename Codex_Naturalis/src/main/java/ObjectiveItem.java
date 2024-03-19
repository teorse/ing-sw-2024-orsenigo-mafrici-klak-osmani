import java.util.Map;

public class ObjectiveItem extends Objective{
    /**
     * Map containing the required resources to satisfy the objective.<br>
     * The Keys of the map are the items and the Values are the number of units required.
     */
    private Map<Item, Integer> requiredItems;

    /**
     * Default Constructor.<br>
     * Defines objective description, number of points held and required Resources.
     * @param description   String description of the Objective.
     * @param points        Int number of points held by the objective.
     * @param requiredItems Map with key required items and value the number of units required.
     */
    public ObjectiveItem(String description, int points, Map<Item, Integer> requiredItems){
        super(description, points);
        this.requiredItems = requiredItems;
    }

    /**
     * Method to access the required Items.
     * @return  Map<Item, Integer> map with Item and number of units required.
     */
    public Map<Item, Integer> getRequiredItems() {
        return requiredItems;
    }
}
