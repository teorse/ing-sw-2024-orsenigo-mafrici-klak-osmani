package Model.Utility.JsonParser.SupportClasses;

import Model.Cards.*;
import Model.Utility.Artifacts;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used by the Json parser. Needed as a middleman between the gson parser and the creation of new CardStarter objects.<br>
 * This class has the same attributes as the target class of CardStarter.<br>
 * It checks that the inputs provided by the gson deserializer are valid and if so proceeds to create a CardStarter object,
 * otherwise it throws an IllegalArgumentException specifying what is wrong with the inputs provided.
 */
public class CardStarterSupportClass {
    //ATTRIBUTES
    private Map<Artifacts, Integer> centralArtifacts;
    private Map<CornerOrientation, CornerSupportClass> corners;





    //VALIDATION METHODS
    /**
     * Method checks if the central artifacts provided are valid.<br>
     * Method ensures that the central artifacts meet the following requirements:<br>
     * -Central artifacts can't be empty.<br>
     * -Central artifacts can't be null.<br>
     * -Central artifacts can't be in negative quantity.<br>
     * If the conditions are broken then throws IllegalArgumentException
     * @return Map containing the constraint after successful validation by the method.
     */
    protected Map<Artifacts, Integer> checkCentralArtifacts(){
        //Central artifacts can't be null.
        if(centralArtifacts == null)
            throw new IllegalArgumentException("centralArtifacts map is null.\n" +
                    "Expected not null map.");

        //Central artifacts can't be empty.
        if(centralArtifacts.isEmpty())
            throw new IllegalArgumentException("centralArtifacts map is empty or has no valid entries.\n" +
                    "Expected non empty map with valid entries");

        //Central artifacts can't be in negative quantity
        for(Map.Entry<Artifacts, Integer> entry : centralArtifacts.entrySet()){
            if(entry.getValue()<0)
                throw new IllegalArgumentException("Provided negative value for central artifact: "+entry.getKey()+"" +
                        "Expected only positive values.");
        }

        return this.centralArtifacts;
    }

    /**
     * Method checks if the provided corners are valid.<br>
     * Method ensures that corners satisfy following requirements:<br>
     * -Corner map is not null.<br>
     * -Corner map is not empty.<br>
     * -All front facing corners are explicitly declared.<br>
     * -All back facing corners are explicitly declared.<br>
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





        //Checks if map contains ALL keys of front and back facing corners.
        CornerDirection[] cornerDirections = new CornerDirection[]{
                CornerDirection.NW,
                CornerDirection.NE,
                CornerDirection.SE,
                CornerDirection.SW
        };

        for(int i = 0; i<4; i++) {
            //Checks for the key on front and back
            if (!corners.containsKey(new CornerOrientation(cornerDirections[i], true)))
                throw new IllegalArgumentException("Missing corner key for:\nCornerDirection: " + cornerDirections[i].toString() + "\nfaceUp: true\n" +
                        "All faceUp corners of Starter Card must be explicitly declared, please review Json file");
            if (!corners.containsKey(new CornerOrientation(cornerDirections[i], false)))
                throw new IllegalArgumentException("Missing corner key for:\nCornerDirection: " + cornerDirections[i].toString() + "\nfaceUp: false\n" +
                        "All faceDown corners of Starter Card must be explicitly declared, please review Json file");

            //Checks that value associated to the key is not null
            if (corners.get(new CornerOrientation(cornerDirections[i], true)) == null)
                throw new IllegalArgumentException("Missing corner value for key:\nCornerDirection: " + cornerDirections[i].toString() + "\nfaceUp: true\n" +
                        "All faceUp corners of Starter Card must be explicitly declared, please review Json file");
            if (corners.get(new CornerOrientation(cornerDirections[i], true)) == null)
                throw new IllegalArgumentException("Missing corner value for key:\nCornerDirection: " + cornerDirections[i].toString() + "\nfaceUp: false\n" +
                        "All faceDown corners of Starter Card must be explicitly declared, please review Json file");
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
     * Creates new CardStarter using the appropriate constructor and giving it as parameters the values
     * returned by the validation methods.
     * @return CardStarter object cast to Card created with parsed and validated inputs.
     */
    public Card createCardStarter(){
        return (Card) new CardStarter(checkCentralArtifacts(), checkCorners());
    }

}
