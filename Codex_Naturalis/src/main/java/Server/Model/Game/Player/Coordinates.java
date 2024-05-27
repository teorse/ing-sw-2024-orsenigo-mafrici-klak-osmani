package Server.Model.Game.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Coordinates class is used to store information about where played cards have been placed by the player
 * using an X and Y coordinates system.
 */

public class Coordinates implements Serializable {
    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -3789012608437142374L;
    private int coordX;
    private int coordY;





    //CONSTRUCTOR
    /**
     * Default Constructor for Model.Player.Coordinates class.
     * @param X int value of X coordinate.
     * @param Y int value of Y coordinate.
     */
    public Coordinates(int X, int Y){
        this.coordX = X;
        this.coordY = Y;
    }





    //GETTERS
    public int getCoordX(){
        return this.coordX;
    }

    public int getCoordY(){
        return this.coordY;
    }





    //METHODS
    /**
     * Returns a new coordinate object where its X and Y values are the sum of the old coordinate and the
     * coordinate given as parameter.<br>
     * Can be used to rapidly calculate new coordinates given a starting point and an
     * offset value.
     * @param offset Coordinates object containing the values to be added (offset) to the current X and Y
     * @return
     */
    public Coordinates add(Coordinates offset){
        return new Coordinates(this.coordX + offset.getCoordX(), this.coordY + offset.getCoordY());
    }





    //OBJECT METHOD OVERRIDES
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates that)) return false;
        return coordX == that.coordX && coordY == that.coordY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordX, coordY);
    }

    @Override
    public String toString(){
        return "X: "+coordX+", Y: "+coordY;
    }
}
