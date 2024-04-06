package Model.Player;

import Model.Cards.Card;
import Model.Cards.CornerDirection;
import Model.Utility.Coordinates;
import Model.Utility.Artifacts;

import java.util.*;

/**
 * This class stores all the information related to the player's placement of cards:<br>
 * -It stores the Map of placed Cards.<br>
 * -It stores the available placements for the next round.<br>
 * -It stores the player's current Resources and Items counters.
 */


public class CardMap {
    //ATTRIBUTES
    /**
     * Map storing the cards previously placed during the game by the player.
     */
    private Map<Coordinates, CardVisibility> cardsPlaced;
    /**
     * A List containing all the allowed coordinates where the player can place his next card.
     */
    private List<Coordinates> availablePlacements;
    /**
     *
     */
    private List<Coordinates> coordinatesPlaced;
    /**
     * A map used as a counter for the Artifacts currently held by the player.
     */
    private Map<Artifacts, Integer> artifactsCounter;





    //CONSTRUCTOR
    /**
     * Default Constructor.<br>
     * Builds Model.Player.CardPlacement Object by initializing the two maps and the Array and adds to the Array
     * the first coordinate [0,0] to allow for the starter card to be placed.
     */
    public CardMap() {
        cardsPlaced = new HashMap<>();
        availablePlacements = new ArrayList<>();
        coordinatesPlaced = new ArrayList<>();
        availablePlacements.add(new Coordinates(0, 0));
        artifactsCounter = new HashMap<>();
        updateAvailablePlacements(new Coordinates(0, 0));
    }





    //GETTERS
    /**
     * Method to get the Hashmap with the placement of the cards played.
     *
     * @return HashMap with player's card placement.
     */
    public List<Coordinates> getCoordinatesPlaced() {
        return Collections.unmodifiableList(coordinatesPlaced);
    }

    /**
     * Method to get ArrayList with coordinates currently available for placement of the next card.
     *
     * @return ArrayList of Model.Utility.Coordinates available for the placement of the next card.
     */
    public List<Coordinates> getAvailablePlacements() {
        return Collections.unmodifiableList(availablePlacements);
    }



    //SETTERS
    /**
     * Method which changes the amount of artifacts in the counter
     * @param artifact
     * @param delta
     */
    public void changeArtifactAmount(Artifacts artifact, int delta) {
        if(artifactsCounter.containsKey(artifact)) {
            artifactsCounter.put(artifact, artifactsCounter.get(artifact)+delta);
        } else {
            artifactsCounter.put(artifact, delta);
        }
    }

    //CLASS SPECIFIC METHODS
    /**
     * Method which places the card in cardsPlaced map, updates resources and items of the player,
     * updates covered corners of nearby cards, updates the list of coordinates placed and calls the method
     * to update the available placements
     * @param cardToPlace
     * @param coordinateIndex
     * @param faceUp
     */
    public void place(Card cardToPlace, int coordinateIndex, boolean faceUp) {
//        //Coordinates of the card to be placed in the map
//        Coordinates placed = availablePlacements.get(coordinateIndex);
//        //CardVisibility of the card to be placed in the map
//        CardVisibility cardVisibilityToPlace = new CardVisibility(cardToPlace, faceUp);
//
//        Coordinates[] offset = new Coordinates[] {
//                new Coordinates(1, 1),   // North East
//                new Coordinates(1, -1),  // South East
//                new Coordinates(-1, 1),  // North West
//                new Coordinates(-1, -1)  // South West
//        };
//
//        CornerDirection [] cornersToCheck = new CornerDirection[] {
//                CornerDirection.SW,
//                CornerDirection.NW,
//                CornerDirection.SE,
//                CornerDirection.NE
//        };
//        //For loop which function is to cover the corners and reducing the amount of artifacts in the counter
//        for(int i = 0; i < 4; i++) {
//            if(cardsPlaced.containsKey(placed.add(offset[i]))) {
//                CardVisibility coveredCard = cardsPlaced.get(placed.add(offset[i]));
//                coveredCard.coverCorner(cornersToCheck[i]);
//                Artifacts coveredArtifact = coveredCard.getCornerArtifact(cornersToCheck[i]);
//                if(coveredArtifact != Artifacts.NULL) {
//                    changeArtifactAmount(coveredArtifact, -1);
//                }
//            }
//        }
//        //For loop which function is to increment the amount of artifacts in the counter
//        Map<Artifacts, Integer> newArtifacts = cardVisibilityToPlace.getAllArtifacts();
//        for(Map.Entry<Artifacts, Integer> entry : newArtifacts.entrySet()){
//            changeArtifactAmount(entry.getKey(), entry.getValue());
//        }
//        //Putting the card in the map
//        cardsPlaced.put(placed, cardVisibilityToPlace);
//        //Updating the list of used coordinates
//        coordinatesPlaced.add(placed);
//        //Calling the method which updates the available placements
//        updateAvailablePlacements(placed);
    }

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
    public int getAmountOfNearbyCorners(Coordinates coordinates) {
//
//        int coveredCorners = 0;
//
//        Coordinates[] offset = new Coordinates[] {
//                new Coordinates(1, 1),   // North East
//                new Coordinates(1, -1),  // South East
//                new Coordinates(-1, 1),  // North West
//                new Coordinates(-1, -1)  // South West
//        };
//
//        for(int i = 0; i < 4; i++) {
//            Coordinates cardPositions = coordinates.add(offset[i]);
//            //If a card exists at the coordinates specified by cardPositions
//            //the card we want to place will cover one corner of the former
//            if(cardsPlaced.containsKey(cardPositions)) {
//                coveredCorners++;
//            }
//        }
//
//        return coveredCorners;
        return 0;
    }

    protected void updateAvailablePlacements(Coordinates coordinates){
    }
}
