/**
 * This class represent the starter cards.<br>
 * It extends the abstract class Card
 */

import java.util.Map;

public class CardStarter extends Card {
    /**
     *Map containing the resources in the center of the card.<br>
     * The Keys of the map are the Resources and the Values are number of units for a given resource.
     */
    private Map<Resource, Integer> centralResources;

    /**
     * Default Constructor, defines a Starter Card by its corners and the centralResources
     * @param corners           Array containing the corners of the card.
     * @param centralResources
     */
    public CardStarter(Corner[] corners, Map<Resource, Integer> centralResources) {
        super(corners);
        this.centralResources = centralResources;
    }

    /**
     * Method to retrieve the central resources of the card.
     * @return  Map<Resource, Integer> where the key is the type of Resource
     * and the value is the number of units of said resource.
     */
    public Map<Resource, Integer> getCentralResources() {
        return centralResources;
    }
}
