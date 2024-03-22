/**
 * This Class is used to realize a nested Map of CardPlacement Objects.<br>
 * It's main implementation in this program is to "Geometrically" represent the cards placed by the player on the
 * "Game Table".<br>
 * It indexes cards based on the coordinates of their position and allows for direct access to them.<br>
 * It is used to check how many points to assign to the player by placing a Card.<br>
 * It is used to check if by placing a card in a given position if it will cover resources/items contained in another
 * card's corner.<br>
 * It is used to check for "Geometric Patterns" when going through Objectives at the end of the game.
 */

import java.util.HashMap;
import java.util.Map;

public class NestedMap {
    /**
     * Nested Map for CardPlacements, indexes Cards based on their X and Y coordinates.
     */
    private Map<Integer, Map<Integer, CardPlacement>> map;

    /**
     * Default Constructor.<br>
     * Initializes the base Map Object as a HashMap.
     */
    public NestedMap(){
        this.map = new HashMap<>();
    }

    /**
     * Adds a CardPlacement object in the position specified by its coordinates.
     * @param card
     */
    public void put(CardPlacement card, Coordinates coordinates){
        Map<Integer, CardPlacement> innerMap;
        int x = coordinates.getCoordX();
        int y = coordinates.getCoordY();
        if(!map.containsKey(x)){
            map.put(x, new HashMap<>());
        }
        innerMap = map.get(x);
        innerMap.put(y, card);
    }

    /**
     * Checks if the position give by the coordinates contains a Card.<br>
     * Returns true if position is NOT empty.<br>
     * Returns false if position is empty.
     * @param coordinates Coordinates to be checked.
     * @return  Boolean, true if position not empty, false if position is empty.
     */
    public boolean containsKey(Coordinates coordinates){
        Map<Integer, CardPlacement> innerMap;
        int x = coordinates.getCoordX();
        int y = coordinates.getCoordY();
        if(map.containsKey(x)){
            innerMap = map.get(x);
            if(innerMap.containsKey(y))
                return true;
        }
        return false;
    }

    /**
     * Returns the CardPlacement Object stored at the position given by the coordinates in the Map but does NOT remove it
     * @param coordinates Coordinates of card to be returned.
     * @return  CardPlacement Object at given coordinates without removing from the Map.
     */
    public CardPlacement get(Coordinates coordinates){
        Map<Integer, CardPlacement> innerMap;
        int x = coordinates.getCoordX();
        int y = coordinates.getCoordY();
        innerMap = map.get(x);
        return innerMap.get(y);
    }

    /**
     * Returns the CardPlacement Object stored at the given coordinates and removes it from the Map
     * @param coordinates Coordinates of card to be returned.
     * @return CardPlacement Object at given coordinates and removes it from the Map.
     */
    public CardPlacement remove(Coordinates coordinates){
        CardPlacement r;
        Map<Integer, CardPlacement> innerMap;
        int x = coordinates.getCoordX();
        int y = coordinates.getCoordY();
        innerMap = map.get(x);
        r = innerMap.remove(y);
        if(innerMap.isEmpty())
            map.remove(x);
        return r;
    }
}