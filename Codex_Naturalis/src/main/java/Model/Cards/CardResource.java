package Model.Cards;

import Model.Player.CardMap;
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
    public boolean isPlaceable(CardMap cardMap) {
        return true;
    }

    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
        if (faceUp)
            return points;
        else
            return 0;
    }

    /**
     * @param faceUp 
     * @return
     */
    public Map<Artifacts, Integer> getAllArtifacts(boolean faceUp) {
        Map<Artifacts,Integer> mapArtifacts = null;
        if (faceUp) {
            Corner corner = null;
            for (CornerOrientation co : corners.keySet()) {
                corner = corners.get(co);
                if (corner != null && corner.getArtifact()!=Artifacts.NULL) {
//                    if ()
//                        mapArtifacts.put(corner.getArtifact(),1);
                }
            }
        }
        else
            mapArtifacts.put(cardColor,1);
        return mapArtifacts;
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
        return cardColor;
    }

    public int getPoints() {
        return points;
    }
}