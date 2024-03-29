package Model.Cards;

import Model.Other.*;
import Model.Utility.*;
import Model.Cards.*;

import java.util.Map;

/**
 *
 */
public class CardGolden extends CardResource {
    //ATTRIBUTES
    /**
     *
     */
    private boolean requiresCorner;
    /**
     *
     */
    private Artifacts requiredArtifact;
    /**
     *
     */
    private Map<Artifacts, Integer> constraint;





    //CONSTRUCTORS
    /**
     *
     */
    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, boolean requiresCorner, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiresCorner = requiresCorner;
        this.constraint = constraint;
    }

    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, Artifacts requiredArtifact, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiredArtifact = requiredArtifact;
        this.constraint = constraint;
        this.requiresCorner = false;
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
