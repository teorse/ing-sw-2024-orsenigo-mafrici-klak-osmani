package it.polimi.ingsw.Server.Model.Game.Cards;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the orientation of a corner on a game card.
 * <p>
 * This class encapsulates the direction and orientation (face up or face down) of a corner on a card.
 */
public class CornerOrientation implements Serializable {

    //ATTRIBUTES
    @Serial
    private static final long serialVersionUID = -4414100234692308498L;
    private CornerDirection cornerDirection;
    private boolean faceUp;





    //CONSTRUCTOR
    /**
     * Constructs a new CornerOrientation with the specified corner direction and orientation.
     *
     * @param cornerDirection The direction of the corner.
     * @param faceUp          True if the corner is face up, false if it is face down.
     */
    public CornerOrientation(CornerDirection cornerDirection, boolean faceUp) {
        this.cornerDirection = cornerDirection;
        this.faceUp = faceUp;
    }





    //GETTER
    /**
     * Returns the direction of the corner.
     *
     * @return The corner direction.
     */
    public CornerDirection getCornerDirection() {
        return cornerDirection;
    }

    /**
     * Checks if the corner is face up.
     *
     * @return True if the corner is face up, false if it is face down.
     */
    public boolean isFaceUp() {
        return faceUp;
    }





    //EQUALS AND HASH
    /**
     * Compares this CornerOrientation with another object for equality.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal based on corner direction and face up orientation, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof CornerOrientation that)
            return faceUp == that.faceUp && cornerDirection == that.cornerDirection;
        else
            return false;
    }

    /**
     * Generates a hash code for this CornerOrientation based on corner direction and face up orientation.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cornerDirection, faceUp);
    }

    /**
     * Returns a string representation of this CornerOrientation.
     *
     * @return A string representation containing the corner direction and face up orientation.
     */
    @Override
    public String toString() {
        return "CornerOrientation{" +
                "cornerDirection=" + cornerDirection +
                ", faceUp=" + faceUp +
                '}';
    }
}
