package Model.Cards;

import Model.Player.CardMap;
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





    //INTERFACE METHODS
    public boolean isPlaceable(CardMap cardMap) {
        return true;
    }

    /**
     * @param cardMap 
     * @param coordinates 
     * @param faceUp 
     * @return
     */
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
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
        CornerOrientation cornerArtifact = new CornerOrientation(direction, faceUp);
        Corner c = null;
        for (CornerOrientation co : this.corners.keySet())
            if (co.equals(cornerArtifact))
                c = this.corners.get(co);
        assert c != null;
        return c.getArtifact();
    }

    /**
     * @return
     */
    public Artifacts getCardColor() {
        return Artifacts.NULL;
    }

    public int getPoints() {
        return 0;
    }





    //OVERRIDE EQUALS AND HASH
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardStarter that)) return false;
        return Objects.equals(centralArtifacts, that.centralArtifacts) && Objects.equals(corners, that.corners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(centralArtifacts, corners);
    }
}