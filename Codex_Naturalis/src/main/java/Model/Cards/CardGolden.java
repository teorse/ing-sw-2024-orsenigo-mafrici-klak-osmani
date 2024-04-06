package Model.Cards;

import Model.Player.CardMap;
import Model.Utility.*;

import java.util.Map;
import java.util.Objects;

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
        this.requiredArtifact = Artifacts.NULL;
    }

    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, Artifacts requiredArtifact, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiredArtifact = requiredArtifact;
        this.constraint = constraint;
        this.requiresCorner = false;
    }

    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiredArtifact = Artifacts.NULL;
        this.constraint = constraint;
        this.requiresCorner = false;
    }





    //INTERFACE METHODS
    public boolean isPlaceable(CardMap cardMap) {
        boolean check = true;
        for (Artifacts a : constraint.keySet())
            check = (constraint.get(a) <= cardMap.getAmountOfArtifacts(a)) && check;
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
                return cardMap.getAmountOfCoveredCorners(coordinates) * this.getPoints();
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
     * This method display all the constraints needed to place the card
     */
    public void displayConstraints (){
        constraint.forEach((a,i) -> System.out.println("Artifact = " + a + ", Quantity = " + i)); }





    //OVERRIDE EQUALS AND HASH
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardGolden that)) return false;
        if (!super.equals(o)) return false;
        return requiresCorner == that.requiresCorner && requiredArtifact == that.requiredArtifact && Objects.equals(constraint, that.constraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), requiresCorner, requiredArtifact, constraint);
    }
}
