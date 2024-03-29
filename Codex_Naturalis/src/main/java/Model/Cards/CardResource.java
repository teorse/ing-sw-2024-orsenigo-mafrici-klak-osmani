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
     *
     */
    public CardResource(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners) {
        this.cardColor = cardColor;
        this.points = points;
        this.corners = corners;
    }

    public CardResource(Artifacts cardColor, Map<CornerOrientation, Corner> corners) {
        this.cardColor = cardColor;
        this.corners = corners;
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