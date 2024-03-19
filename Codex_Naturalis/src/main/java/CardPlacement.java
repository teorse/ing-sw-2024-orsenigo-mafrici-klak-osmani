/**
 * This class will store all the "positional" information of played cards.<br>
 * When any card is played a new object of this class will be instanced and linked to it
 * storing all the information about that card that is contextual to the current game.<br>
 * The class stores the following information about the card it is linked to:<br>
 * -Visibility of the corners (
 */

public class CardPlacement {
    /**
     * Reference to the card that this instance of CardPlacement is linked to.
     */
    private Card card;
    /**
     * Coordinates of the card relative to the Starter Card [0,0].
     */
    private Coordinates coordinates;
    /**
     * Face orientation of the card, if true the card has been placed Face-up.
     */
    private boolean isFaceUp;
    /**
     *Stores information about the visibility of each of the card's corners.<br>
     *If true, the corner is visible, if false, the corner is covered and not visible.<br>
     * cornerVisibility[0] is the North-Western corner of the card and the remaining corners in the Array
     * follow a clockwise pattern, specifically:<br>
     * cornerVisibility[0] = North-West<br>
     * cornerVisibility[1] = North-East<br>
     * cornerVisibility[2] = South-East<br>
     * cornerVisibility[3] = South-West<br>
     */
    private boolean[] cornerVisibility;

    /**
     * Default Constructor.<br>
     * Needs the card it's associated with, its coordinates object, its face orientation and
     * an Array with the visibility of its corners.
     * @param card              Card it is associated with.
     * @param coordinates       Coordinates object with coordinates of card placement.
     * @param isFaceUp          Face orientation of the card.
     * @param cornerVisibility  Array with visibility for each corner.
     */
    public CardPlacement(Card card, Coordinates coordinates, boolean isFaceUp, boolean[] cornerVisibility) {
        this.card = card;
        this.coordinates = coordinates;
        this.isFaceUp = isFaceUp;
        this.cornerVisibility = cornerVisibility;
    }

    /**
     * Method to update corner visibility once a card has been placed and has then been
     * covered by another card.
     * @param index Position of the corner in the Array
     * @param value Value of visibility flag: true corner is visible, false corner is covered.
     */
    public void setCornerVisibility( int index, boolean value){
        if(index >= 4 || index < 0 ){
            throw new IndexOutOfBoundsException("Index of the corner is out of range!");
        }

        this.cornerVisibility[index] = value;
    }

    /**
     * Returns the card associated with this placement data.
     * @return  Card object this placement data is associated to.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Returns boolean value of the card's face orientation: true face is up, false face is down.
     * @return Boolean isFaceUp. if true the face is up, if false the face is covered.
     */
    public boolean isFaceUp() {
        return isFaceUp;
    }

    /**
     * Returns Array with information about the visibility of the card's corners.
     * @return  Boolean Array determining the visibility of the card's corners.
     */
    public boolean[] getCornerVisibility() {
        return cornerVisibility;
    }

    /**
     * Returns the Coordinates object containing the coordinates of the card.
     * @return Coordinates object containing the coordinates of the card.
     */
    public Coordinates getCoordinates(){
        return this.coordinates;
    }
}
