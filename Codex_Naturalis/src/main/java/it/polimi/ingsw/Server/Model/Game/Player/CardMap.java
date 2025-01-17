package it.polimi.ingsw.Server.Model.Game.Player;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardMapRecord;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardVisibilityRecord;
import it.polimi.ingsw.Exceptions.Game.Model.Player.CoordinateIndexOutOfBounds;
import it.polimi.ingsw.Server.Model.Game.Cards.Card;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;
import it.polimi.ingsw.Server.Model.Game.Cards.CornerType;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets.SCPUpdateCardMap;
import it.polimi.ingsw.Server.Model.Lobby.ObserverRelay;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
    private final Map<Coordinates, CardVisibility> cardsPlaced;
    /**
     * A List containing all the allowed coordinates where the player can place his next card.
     */
    private final List<Coordinates> availablePlacements;

    /**
     * Stores the coordinates placed during this game in a chronological order.<br>
     * It can be used by the GUI to more easily reconstruct which card covers which other card by placing them in the same
     * order as in this list.
     */
    private final List<Coordinates> coordinatesPlaced;

    /**
     * List stores all coordinates placed sorted by Y and then by X.<br>
     * It is used by getAmountOfPattern to check for patterns using this list to go through all coordinates in an orderly fashion
     * to avoid making mistakes in the counting.<br>
     * To avoid sorting each time getAmountOfPattern, it is calculated only once by the first request of getAmountOfPattern
     * and is only re-calculated if a .place occurred that modified the cards in the map and set to null the list.
     */
    private List<Coordinates> coordinatesSorted;
    /**
     * A map used as a counter for the Artifacts currently held by the player.
     */
    private final Map<Artifacts, Integer> artifactsCounter;

    private final ObserverRelay gameObserverRelay;
    private final String username;
    private final Logger logger;





    //CONSTRUCTOR
    /**
     * Default Constructor.<br>
     * Builds CardPlacement Object by initializing the two maps and the Array and adds to the Array
     * the first coordinate [0,0] to allow for the starter card to be placed.
     */
    public CardMap(ObserverRelay gameObserverRelay, String username) {
        logger = Logger.getLogger(CardMap.class.getName());
        logger.info("Initializing CardMap for player "+username);

        cardsPlaced = new HashMap<>();
        availablePlacements = new ArrayList<>();
        availablePlacements.add(new Coordinates(0, 0));

        coordinatesPlaced = new ArrayList<>();
        artifactsCounter = new HashMap<>();

        this.gameObserverRelay = gameObserverRelay;
        this.username = username;
        logger.info("CardMap for player "+username+" initialized");
    }





    //GETTERS
    /**
     * Method to get the Hashmap with the placement of the cards played.
     *
     * @return HashMap with player's card placement.
     */
    public List<Coordinates> getCoordinatesPlaced() {
        return new ArrayList<>(coordinatesPlaced);
    }

    /**
     * Method to get ArrayList with coordinates currently available for placement of the next card.
     *
     * @return ArrayList of Model.Utility.Coordinates available for the placement of the next card.
     */
    public List<Coordinates> getAvailablePlacements() {
        return new ArrayList<>(availablePlacements);
    }

    public Map<Artifacts, Integer> getArtifactsCounter(){
        return new HashMap<>(artifactsCounter);
    }

    /**
     * Method to get the amount of artifacts of type "artifacts" held by the player
     * @param   artifact The artifact type of which the amount is to be returned.
     * @return  int corresponding to the number of artifacts of type "artifacts" held by the player
     */
    public int getAmountOfArtifacts(Artifacts artifact) {
        //Verifies if the map contains the specified artifact
        //Returns the number of artifacts of that type
        //If the specified artifact isn't present in the map, it returns 0
        return artifactsCounter.getOrDefault(artifact, 0);
    }

    public Optional<CardVisibility> getCardVisibilityByCoordinate(Coordinates coordinate){
        if(cardsPlaced.containsKey(coordinate))
            return Optional.of(cardsPlaced.get(coordinate));
        else
            return Optional.empty();
    }





    //SETTERS
    /**
     * Method which changes the amount of artifacts in the counter
     * @param artifact
     * @param delta
     */
    private void changeArtifactAmount(Artifacts artifact, int delta) {
        logger.finest("Adding "+delta+" "+artifact);
        if(artifactsCounter.containsKey(artifact)) {
            artifactsCounter.put(artifact, artifactsCounter.get(artifact)+delta);
        } else {
            artifactsCounter.put(artifact, delta);
        }
        logger.finest("Artifact Counter now:\n"+artifactsCounter);
    }





    //CLASS SPECIFIC METHODS
    /**
     * Method which places the card in cardsPlaced map, updates resources and items of the player,
     * updates covered corners of nearby cards, updates the list of coordinates placed, calls the method
     * to update the available placements and returns the points gained by the player by placing this card.
     * It does not check if the given card can or can't be placed according to the rules, it requires the cards to
     * have been already checked on their constraints.
     *
     * @param cardToPlace Card
     * @param coordinateIndex int
     * @param faceUp boolean
     * @return Points earned by the player for placing the card.
     */
    public int place(Card cardToPlace, int coordinateIndex, boolean faceUp) throws CoordinateIndexOutOfBounds {
        logger.info("Player "+username+" is placing a card.");

        //Checks if the provided coordinate index is valid
        if(coordinateIndex < 0 || coordinateIndex >= availablePlacements.size())
            throw new CoordinateIndexOutOfBounds();

        logger.fine("Artifact Counter before place:\n"+artifactsCounter+
                "\nCard that is being placed:\n"+cardToPlace.toRecord()+
                "\nCoordinate where it is being placed: "+availablePlacements.get(coordinateIndex));

        //Coordinates of the card to be placed in the map
        Coordinates placed = availablePlacements.get(coordinateIndex);

        //Checks how many points are awarded for playing this card.
        int points = cardToPlace.countPoints(this, placed, faceUp);

        //CardVisibility of the card to be placed in the map
        CardVisibility cardVisibilityToPlace = new CardVisibility(cardToPlace, faceUp);

        //Calling the method which updates the number of artifacts of the player and the corners to cover
        updateCoveredCornersAndArtifacts(placed);

        //For loop which function is to increment the amount of artifacts in the counter
        Map<Artifacts, Integer> newArtifacts = cardVisibilityToPlace.getAllArtifacts();
        logger.finest("Artifacts found on the card: "+newArtifacts);
        for(Map.Entry<Artifacts, Integer> entry : newArtifacts.entrySet()){
            changeArtifactAmount(entry.getKey(), entry.getValue());
        }

        //Putting the card in the map
        cardsPlaced.put(placed, cardVisibilityToPlace);
        //Updating the list of used coordinates
        coordinatesPlaced.add(placed);
        //Calling the method which updates the available placements
        updateAvailablePlacements(cardVisibilityToPlace, coordinateIndex);

        //Resets coordinates sorted
        coordinatesSorted = null;

        if(gameObserverRelay != null)
            gameObserverRelay.update(new SCPUpdateCardMap(username, this.toTransferableDataObject()));

        logger.fine("Artifact Counter after placing the card: "+artifactsCounter);
        return points;
    }

    /**
     * Method to get the amount of corners that would be covered if a card was to be placed
     * at that coordinates. Precondition: All the coordinates passed as parameters of the function are taken
     * from availablePlacements, so we don't need to check if they're legit or not
     * @param coordinates Coordinates
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
     * Calculates the number of times a specific pattern appears on the card map, ensuring that each card is counted only once.
     *
     * @param pattern A map representing the pattern to be checked. Each entry in the map consists of a coordinate and an artifact that represent the color of the card that should appear at that coordinate.
     * @return The number of times the pattern appears on the card map, ensuring that each card is counted only once.
     */
    public int getAmountOfPattern(Map<Coordinates, Artifacts> pattern){
        if(coordinatesSorted == null){
            coordinatesSorted = new ArrayList<>(cardsPlaced.keySet());
            //Comparator to sort coordinates first by Y coord and then by X coord
            Comparator<Coordinates> comparator = Comparator.comparing(Coordinates::getCoordY);
            comparator.thenComparing(Coordinates::getCoordX);

            //Sort the coordinates placed by the above comparator
            Stream<Coordinates> coordinatesStream = coordinatesSorted.stream().sorted(comparator);
            coordinatesSorted = coordinatesStream.toList();
        }


        //Stores the coordinates of all instances of this objective's pattern found in the cardMap
        List<List<Coordinates>> patternsFound = new ArrayList<>();

        //Iterates over each placed coordinate.
        for (Coordinates currentCoordinate : coordinatesSorted) {
            List<Coordinates> currentPattern = new ArrayList<>();

            //Iterates over each offset of this objective's pattern.
            for (Coordinates offset : pattern.keySet()) {
                Coordinates coordinateWithOffset = currentCoordinate.add(offset);

                if (!cardsPlaced.containsKey(coordinateWithOffset)) {
                    break; // Exit early if the pattern is not found
                }

                if (!pattern.get(offset).equals(cardsPlaced.get(coordinateWithOffset).getCardColor())) {
                    break; // Exit early if the colors don't match
                }

                //Check if the coordinate is not already used.
                boolean coordinateAlreadyUsed = patternsFound.stream()
                        .anyMatch(previousPattern -> previousPattern.contains(coordinateWithOffset));

                //If the coordinateWithOffset was not used in any other pattern then add it to the currentPattern.
                if (!coordinateAlreadyUsed) {
                    currentPattern.add(coordinateWithOffset);
                } else {
                    break; // Move to the next coordinate if already used in another pattern
                }
            }

            if (currentPattern.size() == pattern.size()) {
                //Only if the current pattern contains the same amount of entries as the objectivePattern
                //It is confirmed as valid and added to the patternsFound.
                patternsFound.add(currentPattern);
            }
        }

        return patternsFound.size();
    }

    /**
     * Method which updates the number of artifacts of the player and
     * the corners that will be covered by the card placed
     * @param placed Coordinates
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
                if(coveredArtifact == null) {
                    logger.warning("Found the corner with null artifact, coordinate of the card: "+placed.add(offset[i])+"\n" +
                            "corner with null artifact is: "+cornersToCheck[i]);
                }
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

    /**
     * Converts the current state of the card map into a CardMapRecord object, which can be transferred over the network.
     * This method iterates through the cards placed on the map, converts each card's visibility into a CardVisibilityRecord,
     * and constructs a map of coordinates to card visibility records. It then creates a new CardMapRecord object
     * containing the card placements, available placements, artifacts counter, and coordinates of placed cards.
     *
     * @return the CardMapRecord representing the current state of the card map for transfer over the network
     */
    public CardMapRecord toTransferableDataObject() {
        Map<Coordinates, CardVisibilityRecord> cardsPlaced = new HashMap<>();
        for(Map.Entry<Coordinates, CardVisibility> entry : this.cardsPlaced.entrySet())
            cardsPlaced.put(entry.getKey(), entry.getValue().toRecord());

        return new CardMapRecord(cardsPlaced, availablePlacements, artifactsCounter, coordinatesPlaced);
    }
}
