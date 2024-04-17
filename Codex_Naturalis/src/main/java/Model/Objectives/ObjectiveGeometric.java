package Model.Objectives;

import Model.Player.CardMap;
import Model.Utility.Coordinates;
import Model.Utility.Artifacts;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectiveGeometric extends Objective{
    /**
     * Map containing the required pattern to satisfy the objective.<br>
     * The Keys of the map are the coordinates of the pattern and the Values are the Resource/Color of the pattern.
     */
    private Map<Coordinates, Artifacts> objectivePattern;



    /**
     * Default Constructor.<br>
     * Defines objective description, number of points held and the required Geometric pattern.
     * @param description   String description of the Model.Objectives.Objective.
     * @param points        Int number of points held by the objective.
     * @param objectivePattern   Map with key Model.Utility.Coordinates of the pattern and value resource/color of the pattern.
     */
    public ObjectiveGeometric(String description, int points, Map<Coordinates, Artifacts> objectivePattern){
        super(description, points);
        this.objectivePattern = objectivePattern;
    }

    /**
     * Method to access the required card Pattern.
     * @return  Map<Model.Utility.Coordinates, Resource> map with coordinates and type of resource for the card pattern.
     */
    public Map<Coordinates, Artifacts> getObjectivePattern() {
        return objectivePattern;
    }


    /**
     * Method to iterate the card map and count how many times a certain geometric pattern has been completed.
     * @param cardMap CardMap associated to the player.
     * @return List<List<Model.Utility.Coordinates> patterns that were completed.
     */
    public List<List<Coordinates>> findPatterns(CardMap cardMap){
        Coordinates coordinateWithOffset;
        //Get all the coordinates ever placed in the cardMap
        List<Coordinates> allPlacedCoords = cardMap.getCoordinatesPlaced();
        //Stores the coordinates of all instances of this objective's pattern found in the cardMap
        List<List<Coordinates>> patternsFound = new ArrayList<>();


        //Iterates over each placed coordinate.
        for(Coordinates currentCoordinate : allPlacedCoords){
            List<Coordinates> currentPattern = new ArrayList<>();

            //Iterates over each offset of this objective's pattern.
            for(Coordinates offset : objectivePattern.keySet()){
                coordinateWithOffset = currentCoordinate.add(offset);

                try {
                    Artifacts cardArtifact = cardMap.getCardColorByCoordinate(coordinateWithOffset);


                    //Compare artifact retrieved from cardMap using coordinateWithOffset with artifact that should be
                    //found in this objective's pattern at that offset
                    if(!cardArtifact.equals(objectivePattern.get(offset))){
                        //If the artifacts are different then break the loop and start over with next coordinate.
                        break;
                    }
                    else {
                        boolean coordinateAlreadyUsed = false;
                        //If the artifacts did match then:
                        for (List<Coordinates> previousPattern : patternsFound){
                            if (previousPattern.contains(coordinateWithOffset)){
                                //Check if coordinateWithOffset was already used in any other pattern.
                                coordinateAlreadyUsed = true;
                                break;
                            }
                        }
                        if(!coordinateAlreadyUsed){
                            //If the coordinateWithOffset was not used in any other pattern then add it to the currentPattern.
                            currentPattern.add(coordinateWithOffset);
                        }
                        else{
                            //else, the coordinateWithOffset is not added to the currentPattern and the loop
                            //continues to the next coordinate from allPlacedCoords
                            break;
                        }
                    }
                } catch (NullPointerException e){
                    System.out.println(e);
                }

            }
            if(currentPattern.size() == objectivePattern.size()){
                //Only if the current pattern contains the same amount of entries as the objectivePattern
                //It is confirmed as valid and added to the patternsFound.
                patternsFound.add(currentPattern);
            }
        }

        return patternsFound;
    }



    /**
     * Method to calculate the amount of points given to the player at the end of the game.
     * @param cardMap CardMap associated to the player.
     * @return int number of points.
     */
    public int countPoints(CardMap cardMap){

        return getPoints()*findPatterns(cardMap).size();
    }

}


