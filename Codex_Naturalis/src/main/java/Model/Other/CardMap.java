package Model.Other;

import Model.Utility.Artifacts;

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
    private Map<Coordinates, CardPlacement> cardsPlaced;
    /**
     * A List containing all the allowed coordinates where the player can place his next card.
     */
    private List<Coordinates> availablePlacements;
    /**
     * A map used as a counter for the Artifacts currently held by the player.
     */
    private Map<Artifacts, Integer> artifactsCounter;


    /**
     * Default Constructor.<br>
     * Builds Model.Other.CardPlacement Object by initializing the two maps and the Array and adds to the Array
     * the first coordinate [0,0] to allow for the starter card to be placed.
     */
    public CardMap() {
        cardsPlaced = new HashMap<>();
        availablePlacements = new ArrayList<>();
        availablePlacements.add(new Coordinates(0, 0));
        artifactsCounter = new HashMap<>();
    }

    /**
     * Method to get the Hashmap with the placement of the cards played.
     *
     * @return HashMap with player's card placement.
     */
    public Map<Coordinates, CardPlacement> getCardsPlaced() {
        return cardsPlaced;
    }

    /**
     * Method to get ArrayList with coordinates currently available for placement of the next card.
     *
     * @return ArrayList of Model.Other.Coordinates available for the placement of the next card.
     */
    public List<Coordinates> getAvailablePlacements() {
        return availablePlacements;
    }

    //public void place(Card cardToPlace, int coordinateIndex, boolean faceUp);
    //Adds card to NestedMap cardsPlaced according to the specified coordinate and face orientation.
    //Updates corner visibility of corners of the cards being covered.
    //Updates the player's resources and items counters.

    /**
     * Method to get the amount of artifacts of type "artifacts" held by the player
     * @param artifacts
     * @return int corresponding to the number of artifacts of type "artifacts" held by the player
     */
    public int getAmountOfArtifacts(Artifacts artifacts) {
        //Verifies if the map contains the specified artifact
        if (artifactsCounter.containsKey(artifacts)) {
            //Returns the number of artifacts of that type
            return artifactsCounter.get(artifacts);
        } else {
            //If the specified artifact isn't present in the map, it returns 0
            return 0;
        }
    }


    /**
     * Method to get the amount of corners that would be covered if a card was to be placed
     * at that coordinates
     * @param coordinates
     * @return int corresponding to number of corners that would be covered
     * if a card was to be placed at that coordinates
     */
    public int getAmountOfCoveredCorners(Coordinates coordinates) {
        //Verify if the map contains an instance of CardPlacement for the specified coordinates
        if (cardsPlaced.containsKey(coordinates)) {
            //Get the instance of CardPlacement corresponding to the specified coordinates
            CardPlacement placement = cardsPlaced.get(coordinates);

            //Counts the number of covered corners
            int coveredCorners = 0;
            for (int i = 0; i < 4; i++) {
                if (!placement.getCornerVisibility(i)) {
                    coveredCorners++;
                }
            }

            return coveredCorners;
        } else {
            //If there isn't any CardPlacement for the specified coordinates, it returns 0
            return 0;
        }
    }
    //First two methods return number of specified resources or items from the respective counters.
    //Last method returns number of corners that would be covered if a card was to be placed in that coordinate.
}
