import java.util.Map;

/**
 * The GoldenCard class extends the ResourceCard class by adding constraints to its playability and
 * generally increasing the points awarded when played.
 */

public class GoldenCard extends ResourceCard{
    /**
     * If the field is true then the card will multiply the number of points it will give out by the number of
     * corners being covered when placing it.
     */
    private boolean requiresCorner;
    /**
     * If not null, specifies the item to be used as multiplier for the points awarded to the player.
     */
    private Item requiredItem;
    /**
     * Stores information about the playability constraint;<br>
     * The key of the map is the resource, and the value is an integer indicating how many units of that resource are required.
     */
    private Map<Resource, Integer> constraint;

    /**
     * Constructor for GoldenCard without point multipliers
     * @param corners       Array containing the corners of the card.
     * @param resource      Resource type of the card. Allowed values are: ANIMAL,FUNGI,INSECT,PLANT.
     * @param points        Points awarded by playing the card Face-Up.
     * @param constraint    Map<Resource, Integer>, Key is the required resource, value is the amount of it that is required.
     */
    public GoldenCard(Corner[] corners, Resource resource, int points, Map<Resource, Integer> constraint) {
        super(corners, resource, points);
        this.constraint = constraint;
    }

    /**
     * Constructor for GoldenCard with point multiplier
     * @param corners           Array containing the corners of the card.
     * @param resource          Resource type of the card. Allowed values are: ANIMAL,FUNGI,INSECT,PLANT.
     * @param points            Points awarded by playing the card Face-Up.
     * @param constraint        Map<Resource, Integer>, Key is the required resource, value is the amount of it that is required.
     * @param requiredItem      Item to be used as points multiplier, Allowed values are: INKWELL,MANUSCRIPT,QUILL.
     * @param requiresCorner    Flag, if true means that number of corners covered by placing this card is to be used as points multiplier.
     */
    public GoldenCard(Corner[] corners, Resource resource, int points, Map<Resource, Integer> constraint, Item requiredItem, boolean requiresCorner) {
        super(corners, resource, points);
        this.constraint = constraint;
        this.requiredItem = requiredItem;
        this.requiresCorner = requiresCorner;
    }

    /**
     * Returns value of the requiresCorner flag.
     * @return  value of requiresCorner.
     */
    public boolean requiresCorner() {
        return requiresCorner;
    }

    /**
     * Returns what the required item is.
     * @return  value of requiredItem.
     */
    public Item getRequiredItem() {
        return requiredItem;
    }

    /**
     * Returns the Map containing the constraints for the placement of this card.
     * @return  Map where key is resource, value is required amount.
     */
    public Map<Resource, Integer> getConstraint() {
        return constraint;
    }
}
