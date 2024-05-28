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

public abstract class Card implements Serializable {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = 8378643733472804504L;
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

    public abstract CardRecord toRecord();

    public abstract CardRecord toRecord(boolean faceUp);
}