package Model.Player;

import Model.Cards.Card;
import Model.Cards.CornerDirection;
import Model.Cards.CornerType;
import Model.Utility.Artifacts;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is instanced before playing a card, it stores information about how a card is being played specifically
 * if it's been placed face up or face down and the visibility of its corners when it's being placed and when it will be
 * covered by other cards.
 * This class is then stored in the Model.Player.CardMap. and is what the CardMap class will interact with to talk to
 * the cards.
 */

public class CardVisibility {
    //ATTRIBUTES
    /**
     * Reference to the card that this instance of CardVisibility is linked to.
     */
    private Card card;
    /**
     * Face orientation of the card, if true the card has been placed Face-up.
     */
    private boolean faceUp;
    /**
     *Stores information about the visibility of each of the card's corners.<br>
     *If true, the corner is visible, if false, the corner is covered and not visible.<br>
     */
    private Map<CornerDirection, Boolean> cornerVisibility;





    //CONSTRUCTOR
    /**
     * Default Constructor.<br>
     * Stores information about card's face orientation and sets all corners' visibility to true as by the game's
     * definition when placing a card it can only be placed on top of other cards meaning all of its corners will be visible.
     * @param card              Card this instance of CardVisibility is being instanced for.
     * @param faceUp            Boolean face orientation of the card, if true card is placed face-up.
     */
    public CardVisibility(Card card, boolean faceUp) {
        this.card = card;
        this.faceUp = faceUp;

        cornerVisibility = new HashMap<>(){{
            put(CornerDirection.NW, true);
            put(CornerDirection.NE, true);
            put(CornerDirection.SE, true);
            put(CornerDirection.SW, true);
        }};
    }





    //GETTERS
    /**
     * Returns visibility value of specified corner.
     * @param cornerDirection int index of corner in the Array cornerVisibility to be returned.
     * @return      boolean value of corner at specified index in Array.
     */
    public boolean getCornerVisibility(CornerDirection cornerDirection) {
        return cornerVisibility.get(cornerDirection);
    }

    /**
     * Method that returns the Artifact that is currently found on the visible side of the card on the specified corner.
     * @param cornerDirection   Corner from which to get the Artifact.
     * @return                  Artifact contained in the corner.
     */
    protected Artifacts getCornerArtifact(CornerDirection cornerDirection){
        return card.getCornerArtifact(cornerDirection, faceUp);
    }

    /**
     * Method that returns the CornerType of the specified corner direction currently found on the visible side of the card.
     * @param cornerDirection   Corner from which to get the CornerType
     * @return                  CornerType of the corner
     */
    protected CornerType getCornerType(CornerDirection cornerDirection){
        return card.getCornerType(cornerDirection, faceUp);
    }

    /**
     * Method that returns a Map containing all artifacts on the visible face of the card.<br>
     * It is used when updating the counter in CardMap when a new card is placed to get all the new Artifacts all
     * at once.
     * @return  Map with all Artifacts on card's visible side.
     */
    protected Map<Artifacts, Integer> getAllArtifacts(){
        return card.getAllArtifacts(faceUp);
    }

    /**
     * @return Artifact representing the color of the card.
     */
    protected Artifacts getCardColor(){
        return card.getCardColor();
    }

    /**
     * Returns boolean value of the card's face orientation: true face is up, false face is down.
     * @return Boolean isFaceUp. if true the face is up, if false the face is covered.
     */
    public boolean isFaceUp() {
        return faceUp;
    }





    //SETTERS
    /**
     * Method to update corner visibility once a corner gets covered by a new card being placed on top of it.
     * @param cornerDirection Position of the corner in the Array
     */
    protected void coverCorner(CornerDirection cornerDirection){
        cornerVisibility.put(cornerDirection, false);
    }
}
