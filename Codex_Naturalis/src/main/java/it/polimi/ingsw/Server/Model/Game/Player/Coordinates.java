package it.polimi.ingsw.Server.Model.Game.Player;

import it.polimi.ingsw.Server.Model.Game.Cards.CornerDirection;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

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
     *
     * @param   offset Coordinates object containing the values to be added (offset) to the current X and Y
     *
     * @return  new Coordinate object with coordinates set to old coordinates + provided offset.
     */
    public Coordinates add(Coordinates offset){
        return new Coordinates(this.coordX + offset.getCoordX(), this.coordY + offset.getCoordY());
    }

    /**
     * Determines the relative corner direction of the given coordinate with respect to this coordinate.
     * <p>
     * If the given coordinate is offset by exactly +/-1 on both axes, it returns an appropriate
     * {@link CornerDirection} value. Otherwise, it returns {@link Optional#empty()}.
     * <p>
     * The corner directions are determined as follows:
     * <ul>
     *   <li>{@link CornerDirection#NE} for an offset of (1, 1)</li>
     *   <li>{@link CornerDirection#NW} for an offset of (-1, 1)</li>
     *   <li>{@link CornerDirection#SW} for an offset of (-1, -1)</li>
     *   <li>{@link CornerDirection#SE} for an offset of (1, -1)</li>
     * </ul>
     *
     * @param other the coordinate to compare with this coordinate
     * @return an {@link Optional} containing the {@link CornerDirection} if the offset is valid, otherwise {@link Optional#empty()}
     */
    public Optional<CornerDirection> cornerOfAdjacency(Coordinates other){
        int offsetX = other.getCoordX() - this.coordX;
        int offsetY = other.getCoordY() - this.coordY;

        if (offsetX == 1 && offsetY == 1) {
            return Optional.of(CornerDirection.NE);
        } else if (offsetX == -1 && offsetY == 1) {
            return Optional.of(CornerDirection.NW);
        } else if (offsetX == -1 && offsetY == -1) {
            return Optional.of(CornerDirection.SW);
        } else if (offsetX == 1 && offsetY == -1) {
            return Optional.of(CornerDirection.SE);
        } else {
            return Optional.empty();
        }
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
