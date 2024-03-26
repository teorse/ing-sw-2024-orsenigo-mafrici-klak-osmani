package Model.Cards;

import Model.Cards.*;
import Model.Other.*;
import Model.Utility.*;

import java.util.*;

/**
 * 
 */
public class CardStarter implements Card {

    /**
     * Default constructor
     */
    public CardStarter() {
    }

    /**
     * 
     */
    private Map<Artifacts, Integer> centralArtifacts;

    /**
     * 
     */
    private Map<CornerOrientation, Corner> corners;

    /**
     * 
     */
    protected void CardStarter() {
        // TODO implement here
    }

    /**
     * @return
     */
    public static Card Build() {
        return new CardStarter();
    }

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

    // Setter per centralArtifacts
    public CardStarter setCentralArtifacts(Map<Artifacts, Integer> centralArtifacts) {
        this.centralArtifacts = centralArtifacts;
        return this;
    }

    /**
     * @param faceUp 
     * @return
     */
    public Map<Artifacts, Integer> getAllArtifacts(boolean faceUp) {
        // TODO implement here
        return null;
    }

    public CardStarter setCorners(Map<CornerOrientation, Corner> corners) {
        this.corners = corners;
        return this;
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