package it.polimi.ingsw.Server.Model.Game.Cards;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Player.CardMap;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

public class CardGolden extends CardResource {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -4619796469655619377L;
    /**
     * If this attribute is true then requiredArtifact is set to Artifacts.NULL.
     */
    private boolean requiresCorner;
    private Artifacts requiredArtifact;
    private Map<Artifacts, Integer> constraint;





    //CONSTRUCTORS
    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, boolean requiresCorner, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiresCorner = requiresCorner;
        this.requiredArtifact = Artifacts.NULL;
        this.constraint = constraint;
    }

    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, Artifacts requiredArtifact, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiresCorner = false;
        this.requiredArtifact = requiredArtifact;
        this.constraint = constraint;
    }

    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiresCorner = false;
        this.requiredArtifact = Artifacts.NULL;
        this.constraint = constraint;
    }





    //GETTER
    public boolean requiresCorner() {
        return requiresCorner;
    }
    public Artifacts getRequiredArtifact() {
        return requiredArtifact;
    }
    public Map<Artifacts, Integer> getConstraint() {
        return constraint;
    }





    //ABSTRACT CLASS METHODS
    @Override
    public boolean isPlaceable(CardMap cardMap) {
        boolean check = true;
        for (Artifacts a : constraint.keySet())
            check = (constraint.get(a) <= cardMap.getAmountOfArtifacts(a)) && check;
        return check;
    }

    /*
     * NB: This method should be invoked after the method that updates the artifact counters, adding those present on
     * the played card and removing those covered by it.
     */
    @Override
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
        if (faceUp) {
            if (requiresCorner)
            //Multiply the number of corners covered by the point given for each corner
                return cardMap.getAmountOfNearbyCorners(coordinates) * this.getPoints();
            else if (requiredArtifact != Artifacts.NULL)
            //Multiply the number of artifacts avaible for the player by the point given for each artifact
                    return cardMap.getAmountOfArtifacts(requiredArtifact) * this.getPoints();
                else
                //Gives a fixed amount of points
                    return this.getPoints();
        }
        else
            return 0;
    }





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

    @Override
    public CardRecord toRecord() {
        return new CardRecord(super.getCardColor(), super.getPoints(), super.getCorners(), requiresCorner, requiredArtifact, constraint, null);
    }

    @Override
    public CardRecord toRecord(boolean faceUp){
        return new CardRecord(super.getCardColor(), super.getPoints(), super.getCorners(faceUp), requiresCorner, requiredArtifact, constraint, null);
    }
}