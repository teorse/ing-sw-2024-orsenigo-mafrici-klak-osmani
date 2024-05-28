package it.polimi.ingsw.Utils.JsonParser.SupportClasses;

import it.polimi.ingsw.Server.Model.Game.ArtifactCategories;
import it.polimi.ingsw.Server.Model.Game.Artifacts;
import it.polimi.ingsw.Server.Model.Game.Cards.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used by the Json parser. Needed as a middleman between the gson parser and the creation of new CardResource objects.<br>
 * This class has the same attributes as the target class of CardResource.<br>
 * It checks that the inputs provided by the gson deserializer are valid and if so proceeds to create a CardResource object,
 * otherwise it throws an IllegalArgumentException specifying what is wrong with the inputs provided.
 */
public class CardResourceSupportClass {
    //ATTRIBUTES
    private Artifacts cardColor;
    private int points;
    private Map<CornerOrientation, CornerSupportClass> corners;





    //GETTERS
    /**
     * Method used by CardGoldenSupportClass (class that extends this one) to retrieve the points from this class.
     * @return Int value with points held by card.
     */
    protected int getPoints(){
        return this.points;
    }





    //VALIDATION METHODS
    /**
     * Method checks if the provided points do not contradict the definition of a ResourceCard:<br>
     * -Can't be a negative value.<br>
     * -Has to be either 0 or 1.<br>
     * If the conditions are broken then throws IllegalArgumentException
     * @return Int value with validated points.
     */
    protected int checkPoints(){
        if(points < 0)
            throw new IllegalArgumentException("Provided negative number for card points. Expected 0 or 1 point.");
        if(points > 1)
            throw new IllegalArgumentException("Provided too many points for resource Card. Expected 0 or 1 point.");
        return this.points;
    }

    /**
     * Method checks if the provided color value is not null and if so ensures that only RESOURCE type Artifacts
     * are allowed to be cardColor.<br>
     * If the conditions are broken then throws IllegalArgumentException
     * @return Validated Artifacts enum value of color.
     */
    protected Artifacts checkCardColor(){
        //Checks if cardColor is not null
        if (cardColor == null)
            throw new IllegalArgumentException("cardColor is null or not correctly formatted in Json file.\n" +
                    "CardResource cannot have null cardColor, please review Json file.");

        //Checks if cardColor provided is actually a Resource
        if(!cardColor.getCategories().equals(ArtifactCategories.RESOURCE))
            throw new IllegalArgumentException("cardColor provided is not a resource.\n" +
                    "Color can only be a resource");
        else
            return this.cardColor;
    }

    /**
     * Method checks if the provided corners are valid.<br>
     * Method ensures that corners satisfy following requirements:<br>
     * -Corner map is not null.<br>
     * -Corner map is not empty.<br>
     * -All front facing corners are explicitly declared.<br>
     * -Corner cant have both Artifact and CornerType as null.<br>
     * If the conditions are broken then throws IllegalArgumentException
     * @return Map of validated corners
     */
    protected Map<CornerOrientation, Corner> checkCorners(){
        //Checks if corner map is not null
        if(corners == null)
            throw new IllegalArgumentException("Provided null corners map.\n" +
                    "Expected not null corners map.");

        //Checks if corner map has no elements
        if(corners.isEmpty())
            throw new IllegalArgumentException("Provided empty corners map.\n" +
                    "Expected not empty corners map.");





        //Checks if map contains ALL keys of front facing corners.
        //Back facing corners are not required for ResourceCards.
        CornerDirection[] cornerDirections = new CornerDirection[]{
                CornerDirection.NW,
                CornerDirection.NE,
                CornerDirection.SE,
                CornerDirection.SW
        };
        for(int i = 0; i<4; i++) {

            //Checks for the key
            if (!corners.containsKey(new CornerOrientation(cornerDirections[i], true)))
                throw new IllegalArgumentException("Missing corner key for:\nCornerDirection: " + cornerDirections[i].toString() + "\nfaceUp: true\n" +
                        "All faceUp corners of Resource Card must be explicitly declared, please review Json file");

            //Checks that value associated to the key is not null
            if (corners.get(new CornerOrientation(cornerDirections[i], true)) == null)
                throw new IllegalArgumentException("Missing corner value for key:\nCornerDirection: " + cornerDirections[i].toString() + "\nfaceUp: true\n" +
                        "All faceUp corners of Resource Card must be explicitly declared, please review Json file");
        }





        //Copies individually each corner in the map to the new map
        //It is necessary because old corners are stored in Map <CornerOrientation, CornerSupportClass> to allow for getters
        //and setters of corner values. New map is <CornerOrientation, Corner> to be compatible with CardResource Constructor.
        //
        //
        //entry : entrySet was used as opposed to other for loops in order to copy to the new corner map the corners in the same
        //order as they appear in the Json scan as otherwise two cards with same corners but in different order in the map
        //will not be considered equal.
        Map<CornerOrientation, Corner> cornersChecked = new HashMap<>();

        for(Map.Entry<CornerOrientation, CornerSupportClass> entry : corners.entrySet()){
            CornerSupportClass corner = entry.getValue();


            if(corner.getCornerType() == null){
                    throw new IllegalArgumentException("Missing CornerType value in corner at key:\n"+entry.getKey().toString()+"\n");
            }


            if(corner.getArtifact() == null){
                throw new IllegalArgumentException("Missing Artifact value in corner at key:\n"+entry.getKey().toString()+"\n");
            }

            //After checking that both corner type and artifact are explicitly stated
            //tries to create corner with the values provided.
            try{
                cornersChecked.put(entry.getKey(), new Corner(corner.getCornerType(), corner.getArtifact()));
            }
            catch (IllegalArgumentException message){
                throw new IllegalArgumentException("Incompatible CornerType and Artifact in corner at key:\n"+entry.getKey().toString()+"\n"+
                        message);
            }
        }


        //Returns map with validated corners
        return cornersChecked;
    }





    //CARD CREATOR
    /**
     * Creates new CardResource using the appropriate constructor and giving it as parameters the values
     * returned by the validation methods.
     * @return CardResource object cast to Card created with parsed and validated inputs.
     */
    public Card createCardResource(){

        //Once all the above checks are successfully passed, the card object is now created and returned
         if(points == 0)
             return (Card) new CardResource(checkCardColor(), checkCorners());
         else return (Card) new CardResource(checkCardColor(),checkPoints(), checkCorners());

    }
}
