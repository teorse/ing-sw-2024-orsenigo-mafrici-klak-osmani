package Model.Cards;

import Model.Cards.Corner;
import Model.Cards.CornerDirection;
import Model.Cards.CornerOrientation;
import Model.Player.CardMap;
import Model.Utility.*;

import java.util.Map;
import java.util.Objects;

public abstract class Card {

    //ATTRIBUTES
    private Map<CornerOrientation, Corner> corners;





    //CONSTRUCTOR
    public Card(Map<CornerOrientation, Corner> corners) {
        this.corners = corners;
    }





    //GETTER
    public abstract Artifacts getCardColor();
    public abstract int getPoints();
    public Map<CornerOrientation, Corner> getCorners() {
        return corners;
    }




    //METHODS
    /**
     * For CardResource and CardStarter this method returns always true, because these cards don't have constraints to
     * play the card, while for CardGolden checks if the constraints are satisfied or not.
     *
     * @param cardMap
     * @return true if the card is placeable on both faces, false if can be placed only face down
     */
    public boolean isPlaceable(CardMap cardMap){
        return true;
    }

    /**
     * A generic card gives you points only if is placed faceUp. For CardStarter always returns 0, for CardResource can
     * be either 0 or 1, while for CardGolden calculate the points using the game rules.
     *
     * @param cardMap
     * @param coordinates
     * @param faceUp
     * @return the points due to a specific card placement
     */
    public abstract int countPoints(CardMap cardMap, Coordinates coordinates, boolean faceUp);

    /**
     * This method work in the same way for all the types of card if is placed faceUp, while is different if placed
     * face down. For CardStarter the attribute centralArtifacts contains the back artifacts, instead for CardResource
     * the back artifact is always the same as cardColor.
     *
     * @param faceUp
     * @return a Map which represents the total amount of Artifacts on the choosen side
     */
    public abstract Map<Artifacts, Integer> getAllArtifacts(boolean faceUp);

    /**
     * @param direction
     * @param faceUp
     * @return the Artifact one the required corner
     */
    public Artifacts getCornerArtifact(CornerDirection direction, boolean faceUp) {
        CornerOrientation cornerRequired = new CornerOrientation(direction, faceUp);
        for (CornerOrientation co : this.corners.keySet())
            if (co.equals(cornerRequired))
                return this.corners.get(co).getArtifact();
        return null;
    };

    /**
     * @param direction
     * @param faceUp
     * @return CornerType of the required corner.
     */
    public CornerType getCornerType(CornerDirection direction, boolean faceUp){
        return corners.get(new CornerOrientation(direction, faceUp)).getCornerType();
    }
}