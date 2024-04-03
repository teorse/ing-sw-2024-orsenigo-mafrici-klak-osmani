package Model.Player;

import Model.Cards.Card;

/**
 * This class represents the individual cards held by the player, it stores information about the cards and about their
 * playability, that is, if they can be played only face-down or also face-up.
 */

public class CardPlayability {
    /**
     * Card being held by the player.
     */
    private Card card;
    /**
     * Boolean playability, if true card can be played on both sides, if false card can only be played face down.
     */
    private boolean playability;

    /**
     * Default constructor, creates Model.Player.CardPlayability object by taking a Card and getting a playability value.
     * @param card          Card object.
     * @param playability   Playability value, returned by method in Card class.
     */
    public CardPlayability(Card card, boolean playability){
        this.card = card;
        this.playability = playability;
    }

    /**
     * Method to get the card contained in the object.
     * @return  Card contained in the object.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Method to get Card playability.
     * @return Boolean value of playability, if true card can be played both sides, if false card can be played only
     *         face-down.
     */
    public boolean getPlayability(){
        return this.playability;
    }

    /**
     * Method to set playability value.
     * @param p Boolean value of playability, if true card can be played both sides, if false card can be played only
     *          face-down.
     */
    public void setPlayability(boolean p){
        this.playability = p;
    }
}
