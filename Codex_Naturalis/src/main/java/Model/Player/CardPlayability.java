package Model.Player;

import Model.Cards.Card;

/**
 * This class represents the individual cards held by the player, it stores information about the cards and about their
 * playability, that is, if they can be played only face-down or also face-up.
 */

public class CardPlayability {

    //ATTRIBUTES
    /**
     * Card being held by the player.
     */
    private Card card;
    /**
     * Boolean playability, if true card can be played on both sides, if false card can only be played face down.
     */
    private boolean playability;





    //CONSTRUCTOR
    public CardPlayability(Card card, boolean playability){
        this.card = card;
        this.playability = playability;
    }





    //GETTER
    public Card getCard() {
        return card;
    }
    public boolean getPlayability(){
        return this.playability;
    }





    //METHODS
    /**
     * Method to set playability value at the right value given a CardMap.
     *
     * @param CM represents the actual card on the table
     */
    public void updatePlayability(CardMap CM){
        this.playability = this.card.isPlaceable(CM);
    }
}
