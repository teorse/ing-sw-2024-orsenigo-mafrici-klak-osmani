package it.polimi.ingsw.Server.Model.Game.Cards;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.CardRecord;
import it.polimi.ingsw.Exceptions.Game.Model.CornerNotFoundException;
import it.polimi.ingsw.Server.Model.Game.Player.CardMap;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;
import it.polimi.ingsw.Utils.Utilities;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Abstract class representing a generic card in the game.
 * Specific card types extend this class and provide concrete implementations for methods.
 */
public abstract class Card implements Serializable {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = 8378643733472804504L;
    private Map<CornerOrientation, Corner> corners;





    //CONSTRUCTOR
    /**
     * Constructs a Card with the specified corners.
     *
     * @param corners The corners of the card.
     */
    public Card(Map<CornerOrientation, Corner> corners) {
        this.corners = corners;
    }





    //GETTER
    /**
     * Abstract method to get the color of the card.
     *
     * @return The color of the card as an enum Artifacts.
     */
    public abstract Artifacts getCardColor();

    /**
     * Abstract method to get the points of the card.
     *
     * @return The points of the card.
     */
    public abstract int getPoints();

    /**
     * Returns all corners of the card.
     *
     * @return A map containing all corners of the card.
     */
    public Map<CornerOrientation, Corner> getCorners() {
        return corners;
    }

    /**
     * Returns corners of the card based on whether they are face up or face down.
     *
     * @param faceUp If true, returns face-up corners; if false, returns face-down corners.
     * @return A map containing the corners of the card based on the faceUp parameter.
     */
    public Map<CornerOrientation, Corner> getCorners(boolean faceUp){
        if(faceUp){
            Map<CornerOrientation, Corner> faceUpCorners = new HashMap<>();
            for(Map.Entry<CornerOrientation, Corner> entry : corners.entrySet()){
                if(entry.getKey().isFaceUp())
                    faceUpCorners.put(entry.getKey(), entry.getValue());
            }

            return faceUpCorners;
        }
        else{
            Map<CornerOrientation, Corner> faceDownCorners = new HashMap<>();
            for(Map.Entry<CornerOrientation, Corner> entry : corners.entrySet()){
                if(!entry.getKey().isFaceUp())
                    faceDownCorners.put(entry.getKey(), entry.getValue());
            }

            return faceDownCorners;
        }
    }





    //METHODS
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
     * @param direction
     * @param faceUp
     * @return the Artifact one the required corner
     */
    public Artifacts getCornerArtifact(CornerDirection direction, boolean faceUp) {
        CornerOrientation cornerRequired = new CornerOrientation(direction, faceUp);

        try {
            for (CornerOrientation co : this.corners.keySet())
                if (co.equals(cornerRequired))
                    return this.corners.get(co).getArtifact();
            throw new CornerNotFoundException();
        }

        catch (CornerNotFoundException e) {
            Logger logger = Logger.getLogger(CardResource.class.getName());
            String stackTrace = Utilities.StackTraceToString(e);
            logger.warning("CornerNotFoundException thrown while getting corner artifact from corner: "
                    +direction+" faceUp: "+true+"\nCard with the problem: "+toRecord()+"\nStackTrace: "+stackTrace);
            System.exit(666);
        }
        return null;
    }

    /**
     * @param direction
     * @param faceUp
     * @return CornerType of the required corner.
     */
    public CornerType getCornerType(CornerDirection direction, boolean faceUp){
        return corners.get(new CornerOrientation(direction, faceUp)).getCornerType();
    }

    /**
     * Abstract method to convert the card into a CardRecord for data transfer purposes.
     *
     * @return A CardRecord representing the card.
     */
    public abstract CardRecord toRecord();

    /**
     * Abstract method to convert the card into a CardRecord for data transfer purposes.
     * Includes the option to specify whether the card is face up or face down.
     *
     * @param faceUp Whether the card is recorded as face up or face down.
     * @return A CardRecord representing the card with the specified faceUp status.
     */
    public abstract CardRecord toRecord(boolean faceUp);
}