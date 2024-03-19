/**
 * The ResourceCard class is the most common implementation of the Card class in the game.<br>
 * Resource Cards always have a resource and some of them can give points when placed.
 */

public class ResourceCard extends Card{
    /**
     * Indicates the type of resource this card represents.<br>
     * It is used by the GUI and CLI to determine the color of the card.<br>
     * If the card is played Face-down, the card will give the player 1 unit of the resource stated in the field.
     */
    private Resource resource;
    /**
     * Determines how many points the player will be awarded with upon playing this card Face-up.<br>
     * If the card is golden, playing the card requires the fulfillment of a constraint
     * and the amount of points given will depend on the amount of required resources available/corners covered.
     */
    private int points;

    /**
     * Default Constructor.<br>
     * Creates card with corners, resource but no points.
     * @param corners   Array containing the corners of the card.
     * @param resource  Resource type of the card. Allowed values are: ANIMAL,FUNGI,INSECT,PLANT.
     */
    public ResourceCard(Corner[] corners, Resource resource) {
        super(corners);
        this.resource = resource;
        this.points = 0;
    }

    /**
     * Card WITH points Constructor.<br>
     * Creates card with corners, resource and points.
     * @param corners   Array containing the corners of the card.
     * @param resource  Resource type of the card. Allowed values are: ANIMAL,FUNGI,INSECT,PLANT.
     * @param points    Points awarded by playing the card Face-Up.
     */
    public ResourceCard(Corner[] corners, Resource resource, int points) {
        super(corners);
        this.resource = resource;
        this.points = points;
    }

    /**
     * Returns the resource type of this card
     * @return resource of this card
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Returns the points held by this card.
     * @return value of points field.
     */
    public int getPoints() {
        return points;
    }
}
