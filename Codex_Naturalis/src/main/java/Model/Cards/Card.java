package Model.Cards;

import Model.Other.*;
import Model.Utility.*;

import java.util.Map;

/**
 * 
 */
public interface Card {


    /**
     *  For CardResource and CardStarter this method returns always true, because these cards don't have constraints.
     *
     * @param cardMap
     * @return true if the card is placeable on both faces, false if can be placed only face down
     */
    public boolean isPlaceable(CardMap cardMap);

    /**
     * @param cardMap 
     * @param coordinates 
     * @param faceUp 
     * @return the points due to a specific card placement
     */
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp);

    /**
     * @param faceUp 
     * @return
     */
    public Map<Artifacts, Integer> getAllArtifacts(boolean faceUp);

    /**
     * @param direction 
     * @param faceUp 
     * @return
     */
    public Artifacts getCornerArtifact(CornerDirection direction, boolean faceUp);

    /**
     * @return
     */
    public Artifacts getCardColor();
}