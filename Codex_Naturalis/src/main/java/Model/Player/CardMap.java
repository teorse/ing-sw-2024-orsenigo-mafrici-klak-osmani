package Model.Player;

import Model.Cards.Card;
import Model.Cards.CornerDirection;
import Model.Cards.CornerType;
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
     * Builds CardPlacement Object by initializing the two maps and the Array and adds to the Array
     * the first coordinate [0,0] to allow for the starter card to be placed.
     */
    public CardMap() {
        cardsPlaced = new HashMap<>();
        availablePlacements = new ArrayList<>(){{add(new Coordinates(0, 0));}};
        coordinatesPlaced = new ArrayList<>();
        artifactsCounter = new HashMap<>();
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

    /**
     * Method which returns the map cardsPlaced as an unmodifiable map
     * @return cardsPlaced
     */
    public Map<Coordinates, CardVisibility> getCardsPlaced() {
        return Collections.unmodifiableMap(cardsPlaced);
    }

    /**
     * Method which returns the card color of the card positioned at the specified coordinates.
     * @param coordinates   coordinates of the card.
     * @return              Artifact representing the color of the card.
     */
    public Artifacts getCardColorByCoordinate(Coordinates coordinates) {
        return cardsPlaced.get(coordinates).getCardColor();
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
        //Coordinates of the card to be placed in the map
        Coordinates placed = availablePlacements.get(coordinateIndex);
        //CardVisibility of the card to be placed in the map
        CardVisibility cardVisibilityToPlace = new CardVisibility(cardToPlace, faceUp);

        //Calling the method which updates the number od artifacts of the player and the corners to cover
        updateCoveredCornersAndArtifacts(placed);

        //For loop which function is to increment the amount of artifacts in the counter
        Map<Artifacts, Integer> newArtifacts = cardVisibilityToPlace.getAllArtifacts();
        for(Map.Entry<Artifacts, Integer> entry : newArtifacts.entrySet()){
            changeArtifactAmount(entry.getKey(), entry.getValue());
        }

        //Putting the card in the map
        cardsPlaced.put(placed, cardVisibilityToPlace);
        //Updating the list of used coordinates
        coordinatesPlaced.add(placed);
        //Calling the method which updates the available placements
        updateAvailablePlacements(cardVisibilityToPlace, coordinateIndex);
    }

    /**
     * Method to get the amount of artifacts of type "artifacts" held by the player
     * @param artifacts
     * @return int corresponding to the number of artifacts of type "artifacts" held by the player
     */
    public int getAmountOfArtifacts(Artifacts artifacts) {
        //Verifies if the map contains the specified artifact
        //Returns the number of artifacts of that type
        //If the specified artifact isn't present in the map, it returns 0
        return artifactsCounter.getOrDefault(artifacts, 0);
    }



    /**
     * Method to get the amount of corners that would be covered if a card was to be placed
     * at that coordinates. Precondition: All the coordinates passed as parameters of the function are taken
     * from availablePlacements, so we don't need to check if they're legit or not
     * @param coordinates
     * @return int corresponding to number of corners that would be covered
     * if a card was to be placed at that coordinates
     */
    public int getAmountOfNearbyCorners(Coordinates coordinates) {

        int coveredCorners = 0;

        Coordinates[] offset = new Coordinates[] {
                new Coordinates(1, 1),   // North East
                new Coordinates(1, -1),  // South East
                new Coordinates(-1, 1),  // North West
                new Coordinates(-1, -1)  // South West
        };

        CornerDirection[] cornersToCheck = new CornerDirection[] {
                CornerDirection.SW,
                CornerDirection.NW,
                CornerDirection.SE,
                CornerDirection.NE
        };

        for(int i = 0; i < 4; i++) {
            Coordinates cardPositions = coordinates.add(offset[i]);
            //If the map cardsPlaced contains a card at the coordinates specified by cardPositions
            if(cardsPlaced.containsKey(cardPositions)) {
                //the card we want to place will cover one corner of the former
                coveredCorners++;
            }
        }
        return coveredCorners;
    }

    /**
     * Method which updates the number of artifacts of the player and
     * the corners that will be covered by the card placed
     * @param placed
     */
    protected void updateCoveredCornersAndArtifacts(Coordinates placed) {
        //Array containing the four possible cornerDirections of a corner.
        Coordinates[] offset = new Coordinates[] {
                new Coordinates(1, 1),   // North East
                new Coordinates(1, -1),  // South East
                new Coordinates(-1, 1),  // North West
                new Coordinates(-1, -1)  // South West
        };
        //Array containing the coordinates offset corresponding to each cornerDirection above.
        //The positioning of the offsets in the Array matches that of the cornerDirections so that the
        //offset in the array represents the coordinate to check when analyzing the corner specified by the CornerDirection
        //at the same position in the other Array
        CornerDirection[] cornersToCheck = new CornerDirection[] {
                CornerDirection.SW,
                CornerDirection.NW,
                CornerDirection.SE,
                CornerDirection.NE
        };

        for (int i = 0; i < 4; i++) {
            //If a card is placed in the position placed.add(offset[i])
            if (cardsPlaced.containsKey(placed.add(offset[i]))) {
                //We cover the corner in the cornersToCheck[i] position of that card
                CardVisibility coveredCard = cardsPlaced.get(placed.add(offset[i]));
                coveredCard.coverCorner(cornersToCheck[i]);
                Artifacts coveredArtifact = coveredCard.getCornerArtifact(cornersToCheck[i]);
                //If the covered corner contained an artifact
                if (coveredArtifact != Artifacts.NULL) {
                    //We decrease the amount of that artifact by 1
                    changeArtifactAmount(coveredArtifact, -1);
                }
            }
        }
    }


    /**
     * Method used by .place() in this class to update the available placements after adding a card to the cardsPlaced map.
     * @param card              CardVisibility that was placed using .place() method
     * @param coordinatesIndex  Index of coordinate from availablePlacements used to .place() the card.
     */
    protected void updateAvailablePlacements(CardVisibility card, int coordinatesIndex){

        //Array containing the four possible cornerDirections of a corner.
        CornerDirection[] cornerDirections = new CornerDirection[]{
                CornerDirection.NW,
                CornerDirection.NE,
                CornerDirection.SE,
                CornerDirection.SW
        };

        //Array containing the coordinates offset corresponding to each cornerDirection above.
        //The positioning of the offsets in the Array matches that of the cornerDirections so that the
        //offset in the array represents the coordinate to check when analyzing the corner specified by the CornerDirection
        //at the same position in the other Array.
        Coordinates[] coordinateOffsets = new Coordinates[]{
                new Coordinates(-1,1),
                new Coordinates(1,1),
                new Coordinates(1,-1),
                new Coordinates(-1,-1)
        };

        Coordinates currentCoordinate = availablePlacements.get(coordinatesIndex);
        List<Coordinates> candidates = new ArrayList<>();


        //Adds candidates for new available placements into list.
        for(int i = 0; i < 4; i++){

            Coordinates currentCandidate = currentCoordinate.add(coordinateOffsets[i]);

            //Checks corner Types of the placed card
            //If a null corner is found then:
            //1)Current candidate is not eligible anymore.
            //2)If available placements already contained this candidate, it is removed from available placements.
            if(card.getCornerType(cornerDirections[i]).equals(CornerType.NULL))
                availablePlacements.remove(currentCandidate);

            //If the corner is not null then:
            //If the coordinate is not already used by a placed card
            //If the candidate is not already in available placements then it is considered and eligible candidate and
            //is added to the list of candidates.
            else if(!cardsPlaced.containsKey(currentCandidate) && !availablePlacements.contains(currentCandidate)){
                    candidates.add(currentCandidate);
            }
        }
        //The only candidates left in the list are now candidates linked to not null corners and that were not already
        //present in the available placements list.


        //Further filtering the candidates list.
        for(int i = 0; i < candidates.size(); i++){

            Coordinates candidate = candidates.get(i);
            //For each candidate in the list the code checks its four neighbouring coordinates.
            for(int j = 0; j < 4; j++){
                Coordinates candidateNeighbour = candidate.add(coordinateOffsets[j]);

                //Excluding the neighbour representing the card that initiated this update in the first place
                //the code checks the remaining three neighbouring coordinates.
                //If at least one of these three coordinates already contains a card then the candidate
                // is deemed not eligible by the following reasoning:

                //1)By definition we are only working with placements that were not already in the available placements list.
                //2)All cards when placed will attempt to add as available placements all the coordinates associated to their not null corners.
                //3)Knowing 1 and 2, if a candidate placement is not in the available placements list but also has at least one neighbour
                //  It means that at least one of his neighbours has a null corner -> the candidate is not eligible.
                if(cardsPlaced.containsKey(candidateNeighbour) && !candidateNeighbour.equals(currentCoordinate)){
                    candidates.remove(candidate);
                    i--;
                    break;
                }
            }
        }

        //Adding all the eligible candidates to the available placements list and removing the old coordinate.
        availablePlacements.remove(currentCoordinate);
        availablePlacements.addAll(candidates);
    }
}
