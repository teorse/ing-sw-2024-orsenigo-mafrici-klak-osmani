package Model.Cards;

import Model.Other.*;
import Model.Utility.*;
import Model.Cards.*;

import java.util.Map;

/**
 *
 */
public class CardGolden extends CardResource{
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
     * Constructor is private because all card objects should be created with Factory+Build methods and not through "new".
     */
    private CardGolden() {
        super();
    }

    /**
     * Build method called by Factory class. Returns cardGolden object with all attributes set to null, will be used by Builder
     * class to initialize those attributes.
     * @return  CardGolden object with all fields null.
     */
    protected static Card Build() {
        return new CardGolden();
    }





    //SETTERS
    public CardGolden setRequiresCorner(boolean requiresCorner) {
        this.requiresCorner = requiresCorner;
        return this;
    }

    public CardGolden setRequiredArtifact(Artifacts requiredArtifact) {
        this.requiredArtifact = requiredArtifact;
        return this;
    }

    public CardGolden setConstraint(Map<Artifacts, Integer> constraint) {
        this.constraint = constraint;
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
