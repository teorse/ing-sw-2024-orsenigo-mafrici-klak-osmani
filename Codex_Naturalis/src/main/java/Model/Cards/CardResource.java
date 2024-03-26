package Model.Cards;

import Model.Other.*;
import Model.Utility.*;

import java.util.Map;

/**
 * 
 */
public class CardResource implements Card {
    //ATTRIBUTES
    /**
     * 
     */
    private Artifacts cardColor;
    /**
     * 
     */
    private int points;
    /**
     * 
     */
    private Map<CornerOrientation, Corner> corners;





    //CONSTRUCTORS
    /**
     * Constructor is private because all card objects should be created with Factory+Build methods and not through "new".
     */
    private CardResource() {
    }

    /**
     * Build method called by Factory class. Returns cardResource object with all attributes set to null, will be used by Builder
     * class to initialize those attributes.
     * @return  CardResource object with all fields null.
     */
    protected static Card Build() {
        return new CardResource();
    }





    //SETTERS
    protected CardResource setCardColor(Artifacts cardColor) {
        this.cardColor = cardColor;
        return this;
    }

    protected CardResource setPoints(int points) {
        this.points = points;
        return this;
    }

    protected CardResource setCorners(Map<CornerOrientation, Corner> corners) {
        this.corners = corners;
        return this;
    }





    //INTERFACE METHODS
    /**
     * @param cardMap 
     * @return
     */
    public boolean isPlaceable(CardMap cardMap) {
        // TODO implement here
        return false;
    }

    /**
     * @param cardMap 
     * @param coordinates 
     * @param faceUp 
     * @return
     */
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
        // TODO implement here
        return 0;
    }

    /**
     * @param faceUp 
     * @return
     */
    public Map<Artifacts, Integer> getAllArtifacts(boolean faceUp) {
        // TODO implement here
        return null;
    }

    /**
     * @param direction 
     * @param faceUp 
     * @return
     */
    public Artifacts getCornerArtifact(CornerDirection direction, boolean faceUp) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Artifacts getCardColor() {
        // TODO implement here
        return null;
    }

}