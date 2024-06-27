package it.polimi.ingsw.Server.Model.Game.Cards;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Player.CardMap;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a golden card in the game, which extends the functionality of a resource card.
 * Extends the CardResource class.
 */
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
    /**
     * Constructs a CardGolden with the specified color, points, corners, requirement of a corner, and constraint.
     *
     * @param cardColor       The color of the card.
     * @param points          The points of the card.
     * @param corners         The corners of the card.
     * @param requiresCorner  Whether this card requires a corner to be placed.
     * @param constraint      The constraint specifying required artifacts and their counts.
     */
    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, boolean requiresCorner, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiresCorner = requiresCorner;
        this.requiredArtifact = Artifacts.NULL;
        this.constraint = constraint;
    }

    /**
     * Constructs a CardGolden with the specified color, points, corners, required artifact, and constraint.
     *
     * @param cardColor        The color of the card.
     * @param points           The points of the card.
     * @param corners          The corners of the card.
     * @param requiredArtifact The specific artifact required by this card.
     * @param constraint       The constraint specifying required artifacts and their counts.
     */
    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, Artifacts requiredArtifact, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiresCorner = false;
        this.requiredArtifact = requiredArtifact;
        this.constraint = constraint;
    }

    /**
     * Constructs a CardGolden with the specified color, points, corners, and constraint.
     *
     * @param cardColor   The color of the card.
     * @param points      The points of the card.
     * @param corners     The corners of the card.
     * @param constraint  The constraint specifying required artifacts and their counts.
     */
    public CardGolden(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners, Map<Artifacts, Integer> constraint) {
        super(cardColor, points, corners);
        this.requiresCorner = false;
        this.requiredArtifact = Artifacts.NULL;
        this.constraint = constraint;
    }





    //GETTER
    /**
     * Retrieves whether this card requires a corner to be placed.
     *
     * @return {@code true} if this card requires a corner; {@code false} otherwise.
     */
    public boolean requiresCorner() {
        return requiresCorner;
    }

    /**
     * Retrieves the required artifact by this card.
     *
     * @return The required artifact by this card.
     */
    public Artifacts getRequiredArtifact() {
        return requiredArtifact;
    }

    /**
     * Retrieves the constraint specifying required artifacts and their counts.
     *
     * @return The constraint specifying required artifacts and their counts.
     */
    public Map<Artifacts, Integer> getConstraint() {
        return constraint;
    }





    //ABSTRACT CLASS METHODS
    /**
     * Checks whether this CardGolden is placeable on the card map based on its constraints.
     *
     * @param cardMap The card map to check the constraints against.
     * @return {@code true} if the card can be placed based on its constraints; {@code false} otherwise.
     */
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
    /**
     * Calculates the points of the card based on its placement and faceUp status.
     * If the card is face up and requires a corner, multiplies the number of nearby corners by the points of the card.
     * If the card is face up and requires a specific artifact, multiplies the number of available artifacts by the points of the card.
     * Otherwise, returns the fixed points of the card.
     *
     * @param cardMap     The card map containing the card.
     * @param coordinates The coordinates of the card.
     * @param faceUp      Whether the card is face up or not.
     * @return The points of the card based on its placement and faceUp status.
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
    /**
     * Checks whether this CardGolden is equal to another object.
     * Two CardGolden objects are considered equal if they have the same color, points, corners, requirement of a corner,
     * required artifact, and constraint.
     *
     * @param o The object to compare with this CardGolden.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardGolden that)) return false;
        if (!super.equals(o)) return false;
        return requiresCorner == that.requiresCorner && requiredArtifact == that.requiredArtifact && Objects.equals(constraint, that.constraint);
    }

    /**
     * Generates a hash code for the CardGolden object.
     * The hash code is based on the color, points, corners, requirement of a corner, required artifact, and constraint of the card.
     *
     * @return A hash code value for this CardGolden object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), requiresCorner, requiredArtifact, constraint);
    }

    /**
     * Converts the CardGolden object into a CardRecord for data transfer purposes.
     * This method returns a CardRecord representation of the CardGolden object.
     *
     * @return A CardRecord representing the CardGolden object.
     */
    @Override
    public CardRecord toRecord() {
        return new CardRecord(super.getCardColor(), super.getPoints(), super.getCorners(), requiresCorner, requiredArtifact, constraint, null);
    }

    /**
     * Converts the CardGolden object into a CardRecord for data transfer purposes.
     * This method returns a CardRecord representation of the CardGolden object with the specified faceUp status.
     *
     * @param faceUp Whether to record the card as face up or not.
     * @return A CardRecord representing the CardGolden object with the specified faceUp status.
     */
    @Override
    public CardRecord toRecord(boolean faceUp){
        return new CardRecord(super.getCardColor(), super.getPoints(), super.getCorners(faceUp), requiresCorner, requiredArtifact, constraint, null);
    }
}
