package Server.Model.Game.Objectives;

import Client.Model.Records.ObjectiveRecord;
import Server.Model.Game.Player.CardMap;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objective objective = (Objective) o;
        return points == objective.points && Objects.equals(description, objective.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, points);
    }

    public ObjectiveRecord toRecord() {
        return new ObjectiveRecord(description,points);
    }
}
