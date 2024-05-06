package Model.Objectives;

import Model.Player.CardMap;
import Model.Utility.Artifacts;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ObjectiveNumeric extends Objective{
    @Serial
    private static final long serialVersionUID = 2365780516358935930L;
    /**
     * Map containing the required resources to satisfy the objective.<br>
     * The Keys of the map are the items and the Values are the number of units required.
     */
    private final Map<Artifacts, Integer> requiredItems;

    /**
     * Default Constructor.<br>
     * Defines objective description, number of points held and required Resources.
     * @param description   String description of the Model.Objectives.Objective.
     * @param points        Int number of points held by the objective.
     * @param requiredItems Map with key required items and value the number of units required.
     */
    public ObjectiveNumeric(String description, int points, Map<Artifacts, Integer> requiredItems){
        super(description, points);
        this.requiredItems = requiredItems;
    }

    /**
     * Method to access the required Items.
     * @return  Map<Item, Integer> map with Item and number of units required.
     */
    public Map<Artifacts, Integer> getRequiredItems() {
        return requiredItems;
    }



    /**
     * Method to iterate the card map and count how many times a certain pattern has been completed.
     * @param cardMap CardMap associated to the player.
     * @return int number of points given to the player.
     */

    public int countPoints (CardMap cardMap){

        // Counts how many times the pattern has been completed.
        int itemPatternCounter;

        /*
        * Checks the size of the map to determine the type of pattern is being checked.
        * if size = 1 : the map has only one element.
        */
        if(requiredItems.size() == 1){
            Artifacts requiredArtifact = null;
            for (Artifacts artifact : requiredItems.keySet()){
                requiredArtifact = artifact;
            }

            /*Finds the amount of times the artifact was found in the cardMap
            and divides it by the number you need to create a pattern.*/
            itemPatternCounter = cardMap.getAmountOfArtifacts(requiredArtifact) / requiredItems.get(requiredArtifact);
        }else{
            // List that contains how many times each artifact can be found in the specific CardMap.
            List<Integer> numberOfItems = new ArrayList<>();
            for (Artifacts artifacts : requiredItems.keySet()){
                int numberOfItemsOnMap = cardMap.getAmountOfArtifacts(artifacts);
                numberOfItems.add(numberOfItemsOnMap);
            }

            /*
                The minimum number between the amount of times the artifact was found in the CardMap
                is the amount of times that the pattern has been repeated.
            */
            itemPatternCounter = findMin(numberOfItems);
        }
        return getPoints()*itemPatternCounter;

    }


    /**
     * Method to find the minimum number in a list of integers
     * @param list List<Integer>.
     * @return int minimum number of the list.
     */
    public int findMin (List<Integer> list){
        int min = list.getFirst();

        for (int i = 1; i < list.size(); i++){
            if (list.get(i)< min){
                min = list.get(i);
            }
        }
        return min;
    }





    //EQUALS AND HASH

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectiveNumeric that = (ObjectiveNumeric) o;
        return Objects.equals(requiredItems, that.requiredItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), requiredItems);
    }
}
