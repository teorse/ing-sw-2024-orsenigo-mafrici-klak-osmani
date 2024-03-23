import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores all the information related to the player's placement of cards:<br>
 * -It stores the Map of placed Cards.<br>
 * -It stores the available placements for the next round.<br>
 * -It stores the player's current Resources and Items counters.
 */


public class CardMap {
    /**
     * Map storing the cards previously placed during the game by the player.
     */
    private NestedMap cardsPlaced;
    /**
     * A List containing all the allowed coordinates where the player can place his next card.
     */
    private List<Coordinates> availablePlacements;
    /**
     * A map used as a counter for the Resources currently held by the player.
     */
    private Map<Resource, Integer> resourcesCounter;
    /**
     * A map used as a counter for the Items currently held by the player.
     */
    private Map<Item, Integer> itemsCounter;

    /**
     * Default Constructor.<br>
     * Builds CardPlacement Object by initializing the three maps and the Array and adds to the Array
     * the first coordinate [0,0] to allow for the starter card to be placed.
     */
    public CardMap(){
        cardsPlaced = new NestedMap();
        availablePlacements = new ArrayList<>();
        availablePlacements.add(new Coordinates(0,0));
        resourcesCounter = new HashMap<>();
        itemsCounter = new HashMap<>();
    }

    /**
     * Method to get the Hashmap with the placement of the cards played.
     * @return  DoubleHashMap with player's card placement.
     */
    public NestedMap getCardsPlaced() {
        return cardsPlaced;
    }

    /**
     * Method to get ArrayList with coordinates currently available for placement of the next card.
     * @return ArrayList of Coordinates available for the placement of the next card.
     */
    public List<Coordinates> getAvailablePlacements() {
        return availablePlacements;
    }

    //public void place(Card cardToPlace, int coordinateIndex, boolean faceUp);
    //Adds card to NestedMap cardsPlaced according to the specified coordinate and face orientation.
    //Updates corner visibility of corners of the cards being covered.
    //Updates the player's resources and items counters.

    //public int getAmountOfResource(Resource resource){}
    //public int getAmountOfItem(Item item){}
    //public int getAmountOfCoveredCorners(Coordinates coordinates){}
    //First two methods return number of specified resources or items from the respective counters.
    //Last method returns number of corners that would be covered if a card was to be placed in that coordinate.
}
