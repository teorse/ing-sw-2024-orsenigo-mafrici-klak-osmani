package it.polimi.ingsw.Server.Model.Game.Cards;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Server.Model.Game.Player.CardMap;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a resource card in the game, which has a specific color and points.
 * Extends the abstract class Card.
 */
public class CardResource extends Card {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -8859474130320820205L;
    private Artifacts cardColor;
    private int points;





    //CONSTRUCTOR
    /* Two case: one providing the points, that are set automatically at the same value; while the other one set by
     * default the points to zero when no int attribute is provided. */
    /**
     * Constructs a CardResource with the specified color, points, and corners.
     *
     * @param cardColor The color of the card.
     * @param points    The points of the card.
     * @param corners   The corners of the card.
     */
    public CardResource(Artifacts cardColor, int points, Map<CornerOrientation, Corner> corners) {
        super(corners);
        this.cardColor = cardColor;
        this.points = points;
    }

    /**
     * Constructs a CardResource with the specified color and corners.
     * Points are set to 0 by default.
     *
     * @param cardColor The color of the card.
     * @param corners   The corners of the card.
     */
    public CardResource(Artifacts cardColor, Map<CornerOrientation, Corner> corners) {
        super(corners);
        this.cardColor = cardColor;
        //If missing points attribute is set to zero by default
    }





    //GETTER
    /**
     * Constructs a CardResource with the specified color and corners.
     * Points are set to 0 by default.
     */
    public Artifacts getCardColor(){
        return cardColor;
    };

    /**
     * Returns the points of the card.
     *
     * @return The points of the card.
     */
    public int getPoints(){
        return points;
    };

    //ABSTRACT CLASS METHODS
    /**
     * Calculates the points of the card based on its placement and faceUp status.
     *
     * @param cardMap     The card map containing the card.
     * @param coordinates The coordinates of the card.
     * @param faceUp      Whether the card is face up or not.
     * @return The points of the card.
     */
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
        if (faceUp)
            return this.getPoints();
        else
            return 0;
    };

    /**
     * Retrieves all artifacts associated with the card based on its placement and faceUp status.
     * If faceUp is true, retrieves artifacts from face-up corners only.
     * If faceUp is false, retrieves only the central artifact.
     *
     * @param faceUp Whether to retrieve artifacts from face-up or face-down corners.
     * @return A map containing the artifacts and their counts.
     */
    public Map<Artifacts, Integer> getAllArtifacts(boolean faceUp) {
        Map<CornerOrientation, Corner> corners = this.getCorners();
        Map<Artifacts, Integer> mapArtifacts = new HashMap<>();
        Artifacts art;
        int value;
        if (faceUp) {
            for (CornerOrientation co : corners.keySet()) {
                if (co.isFaceUp()) {
                    art = corners.get(co).getArtifact();
                    if (art != Artifacts.NULL) {
                        if (mapArtifacts.putIfAbsent(art, 1) != null) {
                            value = mapArtifacts.get(art) + 1;
                            mapArtifacts.remove(art);
                            mapArtifacts.put(art, value);
                        }
                    }
                }
            }
        }
        else
            //Every CardResource/Golden don't have any artifacts on back corners, put only the central one
            mapArtifacts.put(this.getCardColor(),1);
        return mapArtifacts;
    }

    /**
     * Retrieves the type of corner based on its direction and faceUp status.
     * If faceUp is false, returns CornerType.EMPTY.
     * Otherwise, delegates to the superclass method to get the corner type.
     *
     * @param direction The direction of the corner.
     * @param faceUp    Whether the corner is face up or not.
     * @return The type of corner.
     */
    @Override
    public CornerType getCornerType(CornerDirection direction, boolean faceUp){
        if(!faceUp)
            return CornerType.EMPTY;
        else
            return super.getCornerType(direction, true);
    }

    /**
     * Retrieves the artifact of the corner based on its direction and faceUp status.
     * If faceUp is false, returns Artifacts.NULL.
     * Otherwise, delegates to the superclass method to get the corner artifact.
     *
     * @param direction The direction of the corner.
     * @param faceUp    Whether the corner is face up or not.
     * @return The artifact of the corner.
     */
    @Override
    public Artifacts getCornerArtifact(CornerDirection direction, boolean faceUp){
        if(!faceUp)
            return Artifacts.NULL;
        else
            return super.getCornerArtifact(direction, true);
    }




    //EQUALS AND HASH
    /**
     * Checks whether this CardResource is equal to another object.
     * Two CardResource objects are considered equal if they have the same color, points, and corners.
     *
     * @param o The object to compare with this CardResource.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardResource that)) return false;
        return points == that.points && cardColor == that.cardColor && Objects.equals(this.getCorners(), that.getCorners());
    }

    /**
     * Generates a hash code for the CardResource object.
     * The hash code is based on the color, points, and corners of the card.
     *
     * @return A hash code value for this CardResource object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cardColor, points, this.getCorners());
    }

    /**
     * Converts the CardResource object into a CardRecord for data transfer purposes.
     * This method returns a CardRecord representation of the CardResource object.
     *
     * @return A CardRecord representing the CardResource object.
     */
    @Override
    public CardRecord toRecord() {
        return new CardRecord(getCardColor(), getPoints(), super.getCorners(), false, null, null, null);
    }

    /**
     * Converts the CardResource object into a CardRecord for data transfer purposes.
     * This method returns a CardRecord representation of the CardResource object.
     *
     * @param faceUp Whether to record the card as face up or not.
     * @return A CardRecord representing the CardResource object with the specified faceUp status.
     */
    @Override
    public CardRecord toRecord(boolean faceUp) {
        return new CardRecord(getCardColor(), getPoints(), super.getCorners(faceUp), false, null, null, null);
    }
}