package Model.Other;

import Model.Cards.Card;

/**
 * This class is instanced before playing a card, it stores information about how a card is being played specifically
 * if it's been placed face up or face down and the visibility of its corners when it's being placed and when it will be
 * covered by other cards.
 * This class is then stored in the Model.Other.CardMap.
 */

public class CardPlacement {
    /**
     * Reference to the card that this instance of Model.Other.CardPlacement is linked to.
     */
    private Card card;
    /**
     * Face orientation of the card, if true the card has been placed Face-up.
     */
    private boolean faceUp;
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
     * Stores information about card's face orientation and sets all corners' visibility to true as by the game's
     * definition when placing a card it can only be placed on top of other cards meaning all of its corners will be visible.
     * @param card              Card this instance of Model.Other.CardPlacement is being instanced for.
     * @param faceUp            Boolean face orientation of the card, if true card is placed face-up.
     */
    public CardPlacement(Card card, boolean faceUp) {
        this.card = card;
        this.faceUp = faceUp;
        for(int i = 0; i<4; i++)
            this.cornerVisibility[i] = true;
    }

    /**
     * Returns the card associated with this placement data.
     * @return  Card object this placement data is associated to.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Method to update corner visibility once a corner gets covered by a new card being placed on top of it.
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
     * Returns visibility value of specified corner.
     * @param index int index of corner in the Array cornerVisibility to be returned.
     * @return      boolean value of corner at specified index in Array.
     */
    public boolean getCornerVisibility(int index) {
        return cornerVisibility[index];
    }

    /**
     * Returns boolean value of the card's face orientation: true face is up, false face is down.
     * @return Boolean isFaceUp. if true the face is up, if false the face is covered.
     */
    public boolean isFaceUp() {
        return faceUp;
    }
}
