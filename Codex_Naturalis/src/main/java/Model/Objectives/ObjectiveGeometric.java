package Model.Objectives;

import Model.Player.CardMap;
import Model.Utility.Coordinates;
import Model.Utility.Artifacts;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

public class ObjectiveGeometric extends Objective{
    @Serial
    private static final long serialVersionUID = 7803477112581081879L;
    /**
     * Map containing the required pattern to satisfy the objective.<br>
     * The Keys of the map are the coordinates of the pattern and the Values are the Resource/Color of the pattern.
     */
    private final Map<Coordinates, Artifacts> objectivePattern;



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
     * Method to calculate the amount of points given to the player at the end of the game.
     * @param cardMap CardMap associated to the player.
     * @return int number of points.
     */
    public int countPoints(CardMap cardMap){
        return getPoints()* cardMap.getAmountOfPattern(objectivePattern);
    }





    //EQUALS AND HASH

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectiveGeometric that = (ObjectiveGeometric) o;
        return Objects.equals(objectivePattern, that.objectivePattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), objectivePattern);
    }
}


