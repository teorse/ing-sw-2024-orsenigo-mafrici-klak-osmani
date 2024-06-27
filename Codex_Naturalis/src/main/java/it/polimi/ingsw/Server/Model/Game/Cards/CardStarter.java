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
 * Represents a starter card in the game, which has multiple corners and central artifacts.
 * The central artifacts can be multiple and are used when the card is placed face down.
 */
public class CardStarter extends Card {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -4633125594886874203L;
    /**
     * Useful when the card is placed back (faceUp == false). Can contain multiple Artifacts, unlike other cards, which
     * always have a resource on the back of the same type as the card itself.
     */
    private Map<Artifacts, Integer> centralArtifacts;





    //CONSTRUCTORS
    /**
     * Constructs a CardStarter with the specified corners and central artifacts.
     *
     * @param corners         The corners of the card.
     * @param centralArtifacts The central artifacts of the card.
     */
    public CardStarter(Map<CornerOrientation, Corner> corners, Map<Artifacts, Integer> centralArtifacts) {
        super(corners);
        this.centralArtifacts = centralArtifacts;
    }





    //GETTER
    /**
     * Returns the color of the card.
     *
     * @return Always returns Artifacts. NULL because CardStarter does not have a specific color.
     */
    public Artifacts getCardColor() {
        return Artifacts.NULL;
    }

    /**
     * Returns the points of the card.
     *
     * @return Always returns 0 because CardStarter does not provide any points.
     */
    public int getPoints() {
        return 0;
    }

    /**
     * Returns the central artifacts of the card.
     *
     * @return A map containing the central artifacts of the card.
     */
    public Map<Artifacts, Integer> getCentralArtifacts() {
        return centralArtifacts;
    }





    //ABSTRACT CLASS METHODS
    /**
     * Counts the points of the card based on its corners and their orientations.
     *
     * @param cardMap     The card map containing the card.
     * @param coordinates The coordinates of the card.
     * @param faceUp      Whether the card is face up or not.
     * @return Always returns 0 because CardStarter does not provide any points.
     */
    public int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp) {
        return 0;
    }

    /**
     * Retrieves all artifacts associated with the card based on its face up status.
     * If faceUp is true, returns artifacts from face-up corners only.
     * If faceUp is false, returns central artifacts followed by artifacts from face-down corners.
     *
     * @param faceUp Determines whether to fetch artifacts from face-up or face-down corners.
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
                        if (mapArtifacts.containsKey(art)) {
                            value = mapArtifacts.get(art) + 1;
                            mapArtifacts.put(art, value);
                        } else
                            mapArtifacts.put(art, 1);
                    }
                }
            }
        } else {
            //Start to add the central artifacts then check if other artifacts in corners are present (no duplicate key)
            for (Artifacts a : this.centralArtifacts.keySet()){
                mapArtifacts.put(a,centralArtifacts.get(a));
            }
            for (CornerOrientation co : corners.keySet()) {
                if (!co.isFaceUp()) {
                    art = corners.get(co).getArtifact();
                    if (art != Artifacts.NULL) {
                        if (mapArtifacts.containsKey(art)) {
                            value = mapArtifacts.get(art) + 1;
                            mapArtifacts.put(art, value);
                        } else
                            mapArtifacts.put(art, 1);
                    }
                }
            }
        }
        return mapArtifacts;
    }





    //EQUALS AND HASH
    /**
     * Checks whether this CardStarter is equal to another object.
     * Two CardStarter objects are considered equal if they have the same central artifacts and corners.
     *
     * @param o The object to compare with this CardStarter.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardStarter that)) return false;
        return Objects.equals(centralArtifacts, that.centralArtifacts) && Objects.equals(this.getCorners(), that.getCorners());
    }

    /**
     * Generates a hash code for the CardStarter object.
     * The hash code is based on the central artifacts and corners of the card.
     *
     * @return A hash code value for this CardStarter object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(centralArtifacts, this.getCorners());
    }

    /**
     * Converts the CardStarter object into a CardRecord for data transfer purposes.
     * This method returns a CardRecord representation of the CardStarter object.
     *
     * @return A CardRecord representing the CardStarter object.
     */
    @Override
    public CardRecord toRecord() {
        return new CardRecord(Artifacts.NULL, 0, super.getCorners(), false, Artifacts.NULL, null, centralArtifacts);
    }

    /**
     * Converts the CardStarter object into a CardRecord for data transfer purposes.
     * This method returns a CardRecord representation of the CardStarter object.
     *
     * @param faceUp Whether to record the card as face up or not.
     * @return A CardRecord representing the CardStarter object with the specified faceUp status.
     */
    @Override
    public CardRecord toRecord(boolean faceUp) {
        return new CardRecord(Artifacts.NULL, 0, super.getCorners(faceUp), false, Artifacts.NULL, null, centralArtifacts);
    }
}