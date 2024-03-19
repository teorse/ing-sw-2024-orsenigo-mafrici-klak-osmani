/**
 * This Class is used to realize a nested HashMap of CardPlacement Objects.<br>
 * It's main implementation in this program is to "Geometrically" represent the cards placed by the player on the
 * "Game Table".<br>
 * It indexes cards based on the coordinates of their position and allows for direct access to them.<br>
 * It will be used by the Controller to check wether by placing a Card in a given position it will cover the corners of
 * other cards.<br>
 * It will be used by the Controller to check for "Geometric Patterns" when going through Objectives at the end of the game.
 */

import java.util.HashMap;
import java.util.Map;

public class DoubleHashMap {
    /**
     * Nested HashMap for CardPlacements, indexes Cards based on their X and Y coordinates.
     */
    private Map<Integer, Map<Integer, CardPlacement>> map;

    /**
     * Default Constructor.<br>
     * Initializes the base HashMap Object.
     */
    public DoubleHashMap(){
        this.map = new HashMap<>();
    }

    /**
     * Adds a CardPlacement object in the position specified by its coordinates.
     * @param card
     */
    public void put(CardPlacement card){
        Map<Integer, CardPlacement> innerMap;
        int x = card.getCoordinates().getCoordX();
        int y = card.getCoordinates().getCoordY();
        if(!map.containsKey(x)){
            map.put(x, new HashMap<>());
        }
        innerMap = map.get(x);
        innerMap.put(y, card);
    }

    /**
     * Checks if the position x,y contains a Card.<br>
     * Returns true if position is NOT empty.<br>
     * Returns false if position is empty.
     * @param x int value for X coordinate.
     * @param y int value for Y coordinate.
     * @return  Boolean, true if position not empty, false if position is empty.
     */
    public boolean containsKey(int x, int y){
        Map<Integer, CardPlacement> innerMap;
        if(map.containsKey(x)){
            innerMap = map.get(x);
            if(innerMap.containsKey(y))
                return true;
        }
        return false;
    }

    /**
     * Returns the CardPlacement Object stored at the x,y position in the HashMap but does NOT remove the object from
     * the HashMap.
     * @param x int value for X coordinate.
     * @param y int value for Y coordinate.
     * @return  CardPlacement Object at x,y position without removing from the HashMap.
     */
    public CardPlacement get(int x, int y){
        Map<Integer, CardPlacement> innerMap;
        innerMap = map.get(x);
        return innerMap.get(y);
    }

    /**
     * Returns the CardPlacement Object stored at the x,y position and removes it the HashMap
     * @param x int value for X coordinate.
     * @param y int value for Y coordinate.
     * @return CardPlacement Object at x,y position and removes from the HashMap.
     */
    public CardPlacement remove(int x, int y){
        CardPlacement r;
        Map<Integer, CardPlacement> innerMap;
        innerMap = map.get(x);
        r = innerMap.remove(y);
        if(innerMap.isEmpty())
            map.remove(x);
        return r;
    }
}