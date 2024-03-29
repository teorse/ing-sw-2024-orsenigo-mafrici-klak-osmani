package Model.Other;

/**
 * The Model.Other.Coordinates class is used to store information about card placement using an X and Y coordinate system.
 */

public class Coordinates {
    /**
     * Stores the X coordinate.
     */
    private int coordX;
    /**
     * Stores the Y coordinate.
     */
    private int coordY;

    /**
     * Default Constructor for Model.Other.Coordinates class.
     * @param X int value of X coordinate.
     * @param Y int value of Y coordinate.
     */
    public Coordinates(int X, int Y){
        this.coordX = X;
        this.coordY = Y;
    }

    /**
     * Returns the integer value of X coordinate.
     * @return  int value of coordinate X.
     */
    public int getCoordX(){
        return this.coordX;
    }

    /**
     * Returns the integer value of Y coordinate.
     * @return  int value of coordinate Y.
     */
    public int getCoordY(){
        return this.coordY;
    }
}
