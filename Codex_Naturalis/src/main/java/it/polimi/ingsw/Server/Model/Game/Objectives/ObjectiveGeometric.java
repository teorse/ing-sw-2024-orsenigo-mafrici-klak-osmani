package it.polimi.ingsw.Server.Model.Game.Objectives;

import it.polimi.ingsw.Server.Model.Game.Player.CardMap;
import it.polimi.ingsw.Server.Model.Game.Player.Coordinates;
import it.polimi.ingsw.Server.Model.Game.Artifacts;

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
     * @return  Map <Model.Utility.Coordinates, Resource> map with coordinates and type of resource for the card pattern.
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
    /**
     * Indicates whether some other object is "equal to" this one.
     * Two ObjectiveGeometric objects are considered equal if they satisfy the following conditions:
     * - They are the same instance (reference equality).
     * - They belong to the same class.
     * - They have the same super class equality.
     * - They have the same objectivePattern.
     *
     * @param o The object to compare this ObjectiveGeometric instance with.
     * @return true if the given object is equal to this ObjectiveGeometric object, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectiveGeometric that = (ObjectiveGeometric) o;
        return Objects.equals(objectivePattern, that.objectivePattern);
    }

    /**
     * Returns a hash code value for the ObjectiveGeometric object.
     * The hash code is computed based on the hash code of its superclass and the objectivePattern attribute.
     *
     * @return A hash code value for this ObjectiveGeometric object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), objectivePattern);
    }
}


