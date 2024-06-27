package it.polimi.ingsw.Server.Model.Game.Objectives;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;
import it.polimi.ingsw.Server.Model.Game.Player.CardMap;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class Objective implements Serializable {
    @Serial
    private static final long serialVersionUID = -5290456116285420582L;
    /**
     * String containing the textual description of the objective.
     */
    private final String description;
    /**
     * Integer containing the amount of points held by the objective.<br>
     * For each separate instance of completion of the objective, the same player can be given the points multiple times.
     */
    private final int points;

    /**
     * Default Constructor.<br>
     * Defines objective description and number of points held.
     * @param description   String description of the Model.Objectives.Objective.
     * @param points        Int number of points held by the objective.
     */
    public Objective(String description, int points){
        this.description = description;
        this.points = points;
    }

    /**
     * Method to get the text description of the objective.
     * @return  String containing objective description.
     */
    public String getDescription() {
        return description;
    }

    protected int getPoints(){
        return points;
    }

    public abstract int countPoints(CardMap cardMap);





    //EQUALS AND HASH

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two Objective objects are considered equal if they satisfy the following conditions:
     * - They are the same instance (reference equality).
     * - They belong to the same class.
     * - They have the same points and description attributes.
     *
     * @param o The object to compare this Objective instance with.
     * @return true if the given object is equal to this Objective object, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objective objective = (Objective) o;
        return points == objective.points && Objects.equals(description, objective.description);
    }

    /**
     * Returns a hash code value for the Objective object.
     * The hash code is computed based on the description and points attributes.
     *
     * @return A hash code value for this Objective object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, points);
    }

    /**
     * Converts the objective into an ObjectiveRecord
     * @return ObjectiveRecord
     */
    public ObjectiveRecord toRecord() {
        return new ObjectiveRecord(description,points);
    }
}
