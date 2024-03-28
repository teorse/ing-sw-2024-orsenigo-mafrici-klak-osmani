package Model.Cards;

import Model.Cards.*;
import Model.Other.*;
import Model.Utility.*;

import java.util.*;

/**
 * 
 */
public class CardStarter implements Card {

    //ATTRIBUTES
    /**
     * 
     */
    private Map<Artifacts, Integer> centralArtifacts;

    /**
     * 
     */
    private Map<CornerOrientation, Corner> corners;

    //CONSTRUCTORS
    /**
     * Default constructor
     */
    public CardStarter(Map<Artifacts, Integer> centralArtifacts, Map<CornerOrientation, Corner> corners) {
        this.centralArtifacts = centralArtifacts;
        this.corners = corners;
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