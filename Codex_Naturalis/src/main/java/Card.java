/**
 * The Card class represents the simplest concept of card
 * which all other types of cards extend.<br>
 * It represents the fundamental unit of gameplay.
*/

public abstract class Card {
    /**
     * Stores information about the type and contents of the card's corners.<br>
     * corners[0] is the North-Western corner of the card and the remaining corners in the Array
     * follow a clockwise pattern, specifically:<br>
     * corners[0] = North-West<br>
     * corners[1] = North-East<br>
     * corners[2] = South-East<br>
     * corners[3] = South-West<br>
     */
    private Corner[] corners;

    /**
     * Default Constructor<br>
     * Creates the card with the array of corners provided.
     * @param corners   Array storing the corners of the card
     */
    public Card(Corner[] corners){
        this.corners = corners;
    }

    /**
     * Returns Array with card's corners.
     * @return Array with card's corners.
     */
    public Corner[] getCorners() {
        return corners;
    }

    //public boolean checkPlacementRequirements(CardMap cardMap)
    //Returns true if, in the given cardMap, the card could be placed both
    //face-up and face-down, returns false if the card can only be placed face-down.

    //public Map getResources(boolean faceUp)
    //returns a map <Resource, Integer> containing the amount of resources depending on if the card has
    //been played face-up or face-down.

    //public Map getItems(boolean faceUp)
    //returns a map <Item, Integer> containing the amount of items depending on if the card has
    //been played face-up or face-down.

    //public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp)
    //returns amount of points by placing the card at that coordinate in that cardMap.
}
