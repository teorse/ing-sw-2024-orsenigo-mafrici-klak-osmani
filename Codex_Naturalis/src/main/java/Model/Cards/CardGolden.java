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
    public boolean isPlaceable(CardMap cardMap) {
        boolean check = true;
        for (Artifacts a : constraint.keySet())
            check = (constraint.get(a) <= cardMap.getAmountOfArtifacts(a)) & check;
        return check;
    }

    /**
     * NB: Questo metodo va invocato successivamente al metodo che aggiorna i contatori degli artefatti, aggiungendo
     * quelli presenti sulla carta giocata e togliendo quelli coperti dalla medesima.
     *
     * @param cardMap
     * @param coordinates
     * @param faceUp
     * @return
     */
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
        if (faceUp) {
            if (!requiresCorner)
            //Multiply the number of artifacts avaible for the player by the point given for each artifact
                return cardMap.getAmountOfArtifacts(requiredArtifact) * this.getPoints();
            else
                //Multiply the number of corners covered by the point given for each corner
                return cardMap.getAmountOfCoveredCorners(coordinates)*this.getPoints();
        }
        else
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

    /**
     * This method display all the constraints needed to place the card
     */
    public void displayConstraints (){
        constraint.forEach((a,i) -> System.out.println("Artifact = " + a + ", Quantity = " + i)); }
    }
